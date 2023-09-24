package com.deemor.ttsai.service.request;

import com.deemor.ttsai.dto.request.RequestDto;
import com.deemor.ttsai.dto.request.RequestPage;
import com.deemor.ttsai.entity.request.Request;
import com.deemor.ttsai.entity.request.RequestStatus;
import com.deemor.ttsai.mapper.RequestMapper;
import com.deemor.ttsai.repository.RequestRepository;
import com.deemor.ttsai.exception.request.RequestNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestPage getRequestsPageable(Integer pageNumber, Integer itemsPerPage) {
        Page<Request> page = requestRepository.findAll(PageRequest.of(pageNumber, itemsPerPage, Sort.by("id").descending()));

        RequestPage result = new RequestPage();
        result.setRequests(requestMapper.mapToDto(page.getContent()));
        result.setTotalNumberOfPages(page.getTotalPages());
        result.setTotalNumberOfElements(Long.valueOf(page.getTotalElements()).intValue());

        return result;
    }

    public RequestDto addRequest(RequestDto requestDto) {
        Request request = requestMapper.mapToEntity(requestDto);
        request.setDateOfCreation(LocalDateTime.now());
        request.setId(null);

        return requestMapper.mapToDto(requestRepository.save(request));
    }

    public void deleteRequestById(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    public void deleteAllRequests() {
        requestRepository.deleteAll();
    }

    public RequestDto updateRequestStatus(Long requestId, RequestStatus status) {
        Request request = requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new);
        request.setRequestStatus(status);
        return requestMapper.mapToDto(requestRepository.save(request));
    }
}