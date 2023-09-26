package com.deemor.ttsai.controller;

import com.deemor.TTSAI.model.VoiceModelApi;
import com.deemor.ttsai.mapper.ElevenlabsMapper;
import com.deemor.ttsai.mapper.VoiceMapper;
import com.deemor.ttsai.service.elevenlabs.ElevenlabsService;
import com.deemor.ttsai.service.voice.VoiceService;
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

    private final VoiceService voiceService;
    private final VoiceMapper voiceMapper;

    @Override
    public ResponseEntity<List<VoiceModelApi>> getAllVoices() {
        return ResponseEntity.ok().body(
                voiceMapper.mapListVoiceDtoToModelApi(voiceService.getAllVoices())
        );
    }

    @Override
    public ResponseEntity<List<VoiceModelApi>> refreshAndGetVoices() {
        return ResponseEntity.ok().body(
                voiceMapper.mapListVoiceDtoToModelApi(voiceService.refreshAndGetVoices())
        );
    }
}
