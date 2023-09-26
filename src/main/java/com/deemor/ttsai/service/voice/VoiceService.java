package com.deemor.ttsai.service.voice;

import com.deemor.ttsai.dto.VoiceDto;
import com.deemor.ttsai.exception.voice.AiVoiceNotFoundException;
import com.deemor.ttsai.mapper.ElevenlabsMapper;
import com.deemor.ttsai.mapper.VoiceMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import com.deemor.ttsai.service.elevenlabs.ElevenlabsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VoiceService {

    private final ElevenlabsMapper elevenlabsMapper;
    private final ElevenlabsService elevenlabsService;
    private final VoiceMapper voiceMapper;
    private final AiVoiceRepository aiVoiceRepository;

    public List<VoiceDto> getAllVoices() {
        return voiceMapper.mapToDto(aiVoiceRepository.findAll());
    }

    public VoiceDto getVoiceByVoiceId(String voiceId) {
        return voiceMapper.mapToDto(aiVoiceRepository.findAiVoiceByVoiceId(voiceId).orElseThrow(AiVoiceNotFoundException::new));
    }

    @Transactional
    public List<VoiceDto> refreshAndGetVoices() {
        List<VoiceDto> voices = elevenlabsMapper.mapListVoiceToDto(Voice.getVoices());
        voices.forEach(v -> v.setName(v.getName().toUpperCase()));

        aiVoiceRepository.deleteAll();
        return voiceMapper.mapToDto(aiVoiceRepository.saveAll(voiceMapper.mapToEntity(voices)));
    }

}
