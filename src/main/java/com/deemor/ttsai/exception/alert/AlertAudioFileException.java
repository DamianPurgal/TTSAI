package com.deemor.ttsai.exception.alert;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AlertAudioFileException extends BusinessException {
    public AlertAudioFileException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Alert audio file exception");
    }
}
