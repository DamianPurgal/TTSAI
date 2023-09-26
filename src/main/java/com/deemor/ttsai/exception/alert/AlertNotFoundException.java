package com.deemor.ttsai.exception.alert;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AlertNotFoundException extends BusinessException {
    public AlertNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Alert not found");
    }
}
