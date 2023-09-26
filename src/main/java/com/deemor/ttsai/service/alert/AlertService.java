package com.deemor.ttsai.service.alert;

import com.deemor.ttsai.configuration.RabbitMqConfiguration;
import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.alert.AlertStatus;
import com.deemor.ttsai.entity.audiofile.AudioFile;
import com.deemor.ttsai.entity.request.Request;
import com.deemor.ttsai.entity.voice.AiVoice;
import com.deemor.ttsai.exception.BusinessException;
import com.deemor.ttsai.exception.request.RequestNotFoundException;
import com.deemor.ttsai.exception.request.RequestToAlertConversionException;
import com.deemor.ttsai.exception.voice.AiVoiceNotFoundException;
import com.deemor.ttsai.mapper.RequestMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import com.deemor.ttsai.repository.AlertRepository;
import com.deemor.ttsai.repository.RequestRepository;
import com.deemor.ttsai.service.elevenlabs.ElevenlabsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @RabbitListener(queues = RabbitMqConfiguration.REQUEST_TO_ALERT_QUEUE_NAME)
    public void listenRequestToAlertQueue(Request message) {
        log.info(String.format("Message read from RequestToAlertQueue: %s, ID: %o", message.getMessage(), message.getId()));

        Request request;
        AiVoice aiVoice;
        try {
            request = requestRepository.findById(message.getId()).orElseThrow(RequestNotFoundException::new);
            aiVoice = aiVoiceRepository.findFirstByName(request.getVoiceType()).orElseThrow(AiVoiceNotFoundException::new);
        } catch (Exception exception) {
            log.warn("Cannot find request or voice: " + exception.getMessage());
            return;
        }

        Voice voice;
        try {
            voice = elevenlabsService.getVoiceById(aiVoice.getVoiceId());
        } catch (Exception exception) {
            log.warn("ElevenLabs connection error [get voice]");
            return;
        }

        Alert alert = requestMapper.mapToAlert(request);
        InputStream generatedAudioFile;
        try {
            generatedAudioFile = voice.generateStream(request.getMessage());
        } catch (Exception exception) {
            log.warn("ElevenLabs connection error [generate audio file]");
            return;
        }

        try {
            alert.setAudioFile(
                    AudioFile.builder()
                            .audioFile(generatedAudioFile.readAllBytes())
                            .fileType("mp3_44100")
                            .build()
            );
        } catch (Exception exception) {
            log.warn("Read generated audio file error");
            return;
        }

        log.info("New alert saved");
        alert.setAlertStatus(AlertStatus.NEW);
        alertRepository.save(alert);
    }

}