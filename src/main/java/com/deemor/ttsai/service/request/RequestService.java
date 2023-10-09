package com.deemor.ttsai.service.request;

import com.deemor.ttsai.configuration.RabbitMqConfiguration;
import com.deemor.ttsai.dto.request.RequestDto;
import com.deemor.ttsai.dto.request.RequestPage;
import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.alert.AlertStatus;
import com.deemor.ttsai.entity.request.Request;
import com.deemor.ttsai.entity.request.RequestStatus;
import com.deemor.ttsai.exception.request.RequestNotValidException;
import com.deemor.ttsai.exception.request.RequestStatusException;
import com.deemor.ttsai.exception.voice.AiVoiceNotFoundException;
import com.deemor.ttsai.mapper.RequestMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import com.deemor.ttsai.repository.AlertRepository;
import com.deemor.ttsai.repository.RequestRepository;
import com.deemor.ttsai.exception.request.RequestNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final AlertRepository alertRepository;
    private final RequestMapper requestMapper;
    private final RabbitTemplate template;
    private final AiVoiceRepository aiVoiceRepository;

    public RequestPage getRequestsPageable(Integer pageNumber, Integer itemsPerPage) {
        Page<Request> page = requestRepository.findAllByRequestStatus(RequestStatus.NEW ,PageRequest.of(pageNumber, itemsPerPage, Sort.by("id").descending()));

        RequestPage result = new RequestPage();
        result.setRequests(requestMapper.mapToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

    public RequestDto addRequest(RequestDto requestDto) {
        Request request = requestMapper.mapToEntity(requestDto);
        if (request != null) {
            if (request.getMessage().isBlank()) {
                throw new RequestNotValidException("Message not valid. Message is blank.");
            }
        } else {
            throw new RequestNotValidException("message is null");
        }

        request.setDateOfCreation(LocalDateTime.now());
        request.setId(null);
        request.setRequestStatus(requestDto.getRequestStatus()); //DEV VERSION. CHANGE: requestDto.getRequestStatus() -> RequestStatus.NEW
        request.setVoiceType(request.getVoiceType().toUpperCase());

        aiVoiceRepository.findFirstByName(request.getVoiceType()).orElseThrow(AiVoiceNotFoundException::new);

        Request savedRequest;

        if (useDuplicatedAlert(requestDto)) {
            request.setRequestStatus(RequestStatus.AUTO_ACCEPTED);
            savedRequest = requestRepository.save(request);
            log.info("OPTIMIZED. DUPLICATED TTS FOUND");
        } else {
            savedRequest = requestRepository.save(request);
            //DEV VERSION
            if (savedRequest.getRequestStatus().equals(RequestStatus.ACCEPTED)) {
                template.convertAndSend(RabbitMqConfiguration.REQUEST_TO_ALERT_QUEUE_NAME, request);
            }
            //END OF DEV VERSION
        }

        return requestMapper.mapToDto(savedRequest);
    }

    private boolean useDuplicatedAlert(RequestDto requestDto) {
        Optional<Alert> duplicatedAlert = alertRepository.findFirstByVoiceTypeAndMessage(requestDto.getVoiceType().toUpperCase(), requestDto.getMessage());
        if (duplicatedAlert.isPresent()) {
            Alert alert = duplicatedAlert.get();
            alert.setAlertStatus(AlertStatus.NEW);
            alertRepository.save(alert);
            return true;
        }

        return false;
    }

    public void deleteRequestById(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    public void deleteAllRequests() {
        requestRepository.deleteAll();
    }

    public RequestDto updateRequestStatus(Long requestId, RequestStatus status) {
        Request request = requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new);

        if (status.equals(RequestStatus.ACCEPTED)) {
            if (!request.getRequestStatus().equals(RequestStatus.ACCEPTED)) {
                request.setRequestStatus(status);
                template.convertAndSend(RabbitMqConfiguration.REQUEST_TO_ALERT_QUEUE_NAME, request);
            }
        }
        request.setRequestStatus(status);

        return requestMapper.mapToDto(requestRepository.save(request));
    }

}
