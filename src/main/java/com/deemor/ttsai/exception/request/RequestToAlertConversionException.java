package com.deemor.ttsai.exception.request;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestToAlertConversionException extends BusinessException {
    public RequestToAlertConversionException() {
        super(HttpStatus.BAD_REQUEST, "Cannot convert request to alert");
    }
}