package com.deemor.ttsai.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    protected final HttpStatus status;
    protected final String message;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
