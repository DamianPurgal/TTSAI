package com.deemor.ttsai.service.alert;

import com.deemor.ttsai.configuration.RabbitMqConfiguration;
import com.deemor.ttsai.dto.alert.AlertDto;
import com.deemor.ttsai.dto.alert.AlertPage;
import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.alert.AlertStatus;
import com.deemor.ttsai.entity.alertevent.AlertEvent;
import com.deemor.ttsai.entity.audiofile.AudioFile;
import com.deemor.ttsai.entity.conversation.Conversation;
import com.deemor.ttsai.entity.request.Request;
import com.deemor.ttsai.entity.voice.AiVoice;
import com.deemor.ttsai.exception.alert.AlertAudioFileException;
import com.deemor.ttsai.exception.alert.AlertNotFoundException;
import com.deemor.ttsai.exception.request.RequestNotFoundException;
import com.deemor.ttsai.exception.voice.AiVoiceNotFoundException;
import com.deemor.ttsai.mapper.AlertMapper;
import com.deemor.ttsai.mapper.RequestMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import com.deemor.ttsai.repository.AlertRepository;
import com.deemor.ttsai.repository.AudioFileRepository;
import com.deemor.ttsai.repository.RequestRepository;
import com.deemor.ttsai.service.elevenlabs.ElevenlabsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AlertService {

    private final ElevenlabsService elevenlabsService;
    private final RequestRepository requestRepository;
    private final AiVoiceRepository aiVoiceRepository;
    private final AlertRepository alertRepository;
    private final RequestMapper requestMapper;
    private final AlertMapper alertMapper;
    private final AudioFileRepository audioFileRepository;

    @RabbitListener(queues = RabbitMqConfiguration.REQUEST_TO_ALERT_QUEUE_NAME)
    @Transactional
    public void listenRequestToAlertQueue(Request message) {
        log.info(String.format("Message read from RequestToAlertQueue, ID: %d", message.getId()));

        try {
            Request request = requestRepository.findById(message.getId()).orElseThrow(RequestNotFoundException::new);

            Set<AiVoice> aiVoices = findAllVoicesOfConversation(message.getConversation());
            Set<Voice> voices = elevenlabsService.getVoices(aiVoices.stream().map(aiVoice -> aiVoice.getVoiceId()).collect(Collectors.toSet()));

            createAndSaveAlert(request, voices);

        } catch (Exception exception) {
            log.error(String.format("[ALERT QUEUE] REQUEST_ID: %d, ERROR : %s", message.getId(), exception.getMessage()));
        } finally {
            log.info(String.format("Message processed RequestToAlertQueue, ID: %d", message.getId()));
        }
    }

    private Set<AiVoice> findAllVoicesOfConversation(List<Conversation> conversation) {
        Set<String> voices = conversation.stream()
                .map(Conversation::getVoiceType)
                .collect(Collectors.toSet());

        Set<AiVoice> aiVoices = aiVoiceRepository.findAllByNameIn(voices);

        if (aiVoices.size() != voices.size()) {
            throw new AiVoiceNotFoundException();
        }

        return aiVoices;
    }

    private Alert createAndSaveAlert(Request request, Set<Voice> voices) {
        Alert alert = requestMapper.mapToAlert(request);
        alert.setAlertEvents(new ArrayList<>());
        List<AudioFile> audioFiles = getAudioFiles(request.getConversation(), voices);

        request.getConversation().forEach(element -> {
            AlertEvent event = AlertEvent.builder()
                    .id(null)
                    .orderIndex(Long.valueOf(element.getOrderIndex()))
                    .audioFile(
                            audioFiles.stream()
                                    .filter(audio -> audio.getMessage().equals(element.getMessage()))
                                    .findFirst()
                                    .orElseThrow(AlertAudioFileException::new)
                    ).alert(alert)
                    .build();
            alert.getAlertEvents().add(event);
        });

        alert.setAlertStatus(AlertStatus.NEW);

        return alertRepository.save(alert);
    }

    private List<AudioFile> getAudioFiles(List<Conversation> conversation, Set<Voice> voices) {
        List<AudioFile> audioFiles = new ArrayList<>();

        conversation.forEach(message -> {
            Optional<AudioFile> foundAudioFile = audioFileRepository.findFirstByMessageAndVoiceType(message.getMessage(), message.getVoiceType());

            if (foundAudioFile.isPresent()) {
                audioFiles.add(foundAudioFile.get());
                log.info(String.format("[GENERATE AUDIO] OPTIMIZED! AudioFile ID: %d", foundAudioFile.get().getId()));

            } else {
                AudioFile audioFile = generateAudioFile(
                        message,
                        voices.stream()
                                .filter(element -> element.getName().toUpperCase().equals(message.getVoiceType()))
                                .findFirst()
                                .orElseThrow(AiVoiceNotFoundException::new)
                );
                audioFiles.add(audioFileRepository.save(audioFile));
            }
        });

        return audioFiles;
    }

    public AudioFile generateAudioFile(Conversation conversation, Voice voice) {
        try (InputStream audioFileInputStream = elevenlabsService.generateAudioFile(conversation.getMessage(), voice)) {
            return AudioFile.builder()
                    .audioFile(audioFileInputStream.readAllBytes())
                    .message(conversation.getMessage())
                    .voiceType(conversation.getVoiceType())
                    .fileType("mp3_44100")
                    .build();

        } catch (Exception exception) {
            throw new AlertAudioFileException();
        }
    }

    public AlertDto getAlertById(Long id) {
        return alertMapper.mapToDto(
                alertRepository.findById(id).orElseThrow(AlertNotFoundException::new)
        );
    }

    public AlertPage getAlertsPageable(Integer pageNumber, Integer itemsPerPage) {
        Page<Alert> page = alertRepository.findAllByAlertStatus(AlertStatus.NEW, PageRequest.of(pageNumber, itemsPerPage, Sort.by("id").descending()));

        AlertPage result = new AlertPage();
        result.setRequests(alertMapper.mapToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

    @Transactional
    public AlertDto getAlertLatest() {
        Alert alert = alertRepository.findFirstByAlertStatusOrderByDateOfCreationAsc(AlertStatus.NEW).orElseThrow(AlertNotFoundException::new);
        alert.setAlertStatus(AlertStatus.DISPLAYED);
        alert = alertRepository.save(alert);
        return alertMapper.mapToDto(alert);
    }

}
