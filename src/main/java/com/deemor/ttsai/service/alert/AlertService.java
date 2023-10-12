package com.deemor.ttsai.service.alert;

import com.deemor.ttsai.configuration.RabbitMqConfiguration;
import com.deemor.ttsai.dto.alert.AlertDto;
import com.deemor.ttsai.dto.alert.AlertPage;
import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.alert.AlertStatus;
import com.deemor.ttsai.entity.audiofile.AudioFile;
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
import com.deemor.ttsai.repository.RequestRepository;
import com.deemor.ttsai.service.elevenlabs.ElevenlabsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;

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

    @RabbitListener(queues = RabbitMqConfiguration.REQUEST_TO_ALERT_QUEUE_NAME)
    public void listenRequestToAlertQueue(Request message) {
        log.info(String.format("Message read from RequestToAlertQueue: %s, ID: %d", message.getMessage(), message.getId()));

        try {
            Request request = requestRepository.findById(message.getId()).orElseThrow(RequestNotFoundException::new);
            AiVoice aiVoice = aiVoiceRepository.findFirstByName(request.getVoiceType()).orElseThrow(AiVoiceNotFoundException::new);

            Alert alert = createAlert(request, aiVoice.getVoiceId());
            alertRepository.save(alert);

        } catch (Exception exception) {
            log.info(String.format("[ALERT QUEUE] REQUEST_ID: %d, ERROR : %s", message.getId(), exception.getMessage()));
        } finally {
            log.info(String.format("Message processed: %s, ID: %d", message.getMessage(), message.getId()));
        }
    }

    private Alert createAlert(Request request, String voiceId) {
        Alert alert = requestMapper.mapToAlert(request);

        try (InputStream audioFileInputStream = elevenlabsService.generateAudioFile(request.getMessage(), voiceId)) {
            alert.setAudioFile(
                    AudioFile.builder()
                            .audioFile(audioFileInputStream.readAllBytes())
                            .fileType("mp3_44100")
                            .build()
            );
        } catch (Exception exception) {
            throw new AlertAudioFileException();
        }

        alert.setAlertStatus(AlertStatus.NEW);
        return alert;
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

    public AlertDto getAlertLatest() {
        Alert alert = alertRepository.findFirstByAlertStatusOrderByDateOfCreationAsc(AlertStatus.NEW).orElseThrow(AlertNotFoundException::new);
        alert.setAlertStatus(AlertStatus.DISPLAYED);
        alert = alertRepository.save(alert);
        return alertMapper.mapToDto(alert);
    }

}
