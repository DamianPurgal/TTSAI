package com.deemor.ttsai.controller;

import com.deemor.TTSAI.api.AlertApi;
import com.deemor.TTSAI.model.AlertModelApi;
import com.deemor.TTSAI.model.AlertPageModelApi;
import com.deemor.TTSAI.model.RequestPageableModelApi;
import com.deemor.ttsai.mapper.AlertMapper;
import com.deemor.ttsai.service.alert.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AlertController implements AlertApi {

    private final AlertService alertService;
    private final AlertMapper alertMapper;

    @Override
    public ResponseEntity<AlertModelApi> getAlertById(Long alertId) {
        return ResponseEntity.ok().body(
                alertMapper.mapModelApiToDto(alertService.getAlertById(alertId))
        );
    }

    @Override
    public ResponseEntity<AlertPageModelApi> getAlertsPageable(RequestPageableModelApi requestDto) {
        return ResponseEntity.ok().body(
                alertMapper.mapPageToModelApi(alertService.getAlertsPageable(requestDto.getPage(), requestDto.getSize()))
        );
    }
}
