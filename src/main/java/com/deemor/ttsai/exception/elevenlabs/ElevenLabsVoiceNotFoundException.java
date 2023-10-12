package com.deemor.ttsai.exception.elevenlabs;

import com.deemor.ttsai.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ElevenLabsVoiceNotFoundException extends BusinessException {
    public ElevenLabsVoiceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ElevenLabs voice not found");
    }
}