package com.deemor.ttsai.controller;

import com.deemor.TTSAI.model.VoiceModelApi;
import com.deemor.ttsai.mapper.ElevenlabsMapper;
import com.deemor.ttsai.service.elevenlabs.ElevenlabsService;
import lombok.RequiredArgsConstructor;
import com.deemor.TTSAI.api.VoiceApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class VoiceController implements VoiceApi {

    private final ElevenlabsService elevenlabsService;
    private final ElevenlabsMapper elevenlabsMapper;

    @Override
    public ResponseEntity<List<VoiceModelApi>> getAllVoices() {
        return ResponseEntity.ok().body(
                elevenlabsMapper.mapListVoiceDtoToModelApi(elevenlabsService.getAllVoices())
        );
    }

}
