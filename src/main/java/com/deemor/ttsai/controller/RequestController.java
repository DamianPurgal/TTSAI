package com.deemor.ttsai.controller;

import com.deemor.TTSAI.api.RequestApi;
import com.deemor.TTSAI.model.RequestModelApi;
import com.deemor.TTSAI.model.RequestPageModelApi;
import com.deemor.TTSAI.model.RequestPageableModelApi;
import com.deemor.ttsai.entity.request.RequestStatus;
import com.deemor.ttsai.mapper.RequestMapper;
import com.deemor.ttsai.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class RequestController implements RequestApi {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @Override
    public ResponseEntity<RequestPageModelApi> getRequestsPageable(RequestPageableModelApi requestDto) {
        return ResponseEntity.ok().body(
                requestMapper.mapRequestPageToModelApi(
                        requestService.getRequestsPageable(requestDto.getPage(), requestDto.getSize())
                )
        );
    }

    @Override
    public ResponseEntity<RequestModelApi> addRequest(RequestModelApi requestModelApi) {
        return ResponseEntity.ok().body(
                requestMapper.mapDtoToModelApi(
                        requestService.addRequest(requestMapper.mapDtoToModelApi(requestModelApi))
                )
        );
    }

    @Override
    public ResponseEntity<Void> deleteAllRequests() {
        requestService.deleteAllRequests();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteRequestById(Long requestId) {
        requestService.deleteRequestById(requestId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<RequestModelApi> updateRequestStatus(Long requestId, String status) {
        RequestStatus requestStatus = RequestStatus.valueOf(status);
        return ResponseEntity.ok().body(
                requestMapper.mapDtoToModelApi(
                        requestService.updateRequestStatus(requestId, requestStatus)
                )
        );
    }

}
