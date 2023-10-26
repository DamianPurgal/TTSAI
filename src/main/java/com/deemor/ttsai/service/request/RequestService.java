package com.deemor.ttsai.service.request;

import com.deemor.ttsai.configuration.RabbitMqConfiguration;
import com.deemor.ttsai.dto.conversation.ConversationDto;
import com.deemor.ttsai.dto.request.RequestDto;
import com.deemor.ttsai.dto.request.RequestPage;
import com.deemor.ttsai.entity.request.Request;
import com.deemor.ttsai.entity.request.RequestStatus;
import com.deemor.ttsai.exception.request.RequestNotFoundException;
import com.deemor.ttsai.exception.voice.AiVoiceNotFoundException;
import com.deemor.ttsai.mapper.RequestMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import com.deemor.ttsai.repository.RequestRepository;
import com.deemor.ttsai.service.request.util.RequestServiceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RabbitTemplate template;
    private final AiVoiceRepository aiVoiceRepository;

    public RequestPage getRequestsPageable(Integer pageNumber, Integer itemsPerPage) {
        Page<Request> page = requestRepository.findAllByRequestStatus(RequestStatus.NEW, PageRequest.of(pageNumber, itemsPerPage, Sort.by("id").descending()));

        RequestPage result = new RequestPage();
        result.setRequests(requestMapper.mapToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

    public RequestDto addRequest(RequestDto requestDto) {
        validateRequest(requestDto);
        requestDto.getConversation().forEach(message -> message.setVoiceType(message.getVoiceType().toUpperCase()));

        Request request = requestMapper.mapToEntity(requestDto);
        request.setRequestStatus(RequestStatus.ACCEPTED);
        Request savedRequest = requestRepository.save(request);

        template.convertAndSend(RabbitMqConfiguration.REQUEST_TO_ALERT_QUEUE_NAME, savedRequest);
        return requestMapper.mapToDto(savedRequest);
    }

    private void validateRequest(RequestDto request) {
        RequestServiceUtil.validateConversation(request.getConversation());
        request.getConversation().stream()
                .map(ConversationDto::getVoiceType)
                .map(String::toUpperCase)
                .collect(Collectors.toSet())
                .forEach(voice -> aiVoiceRepository.findFirstByName(voice).orElseThrow(AiVoiceNotFoundException::new));
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
