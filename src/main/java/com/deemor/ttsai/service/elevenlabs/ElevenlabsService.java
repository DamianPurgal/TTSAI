package com.deemor.ttsai.service.elevenlabs;

import com.deemor.ttsai.dto.VoiceDto;
import com.deemor.ttsai.mapper.ElevenlabsMapper;
import com.deemor.ttsai.mapper.VoiceMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import lombok.AllArgsConstructor;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@AllArgsConstructor
public class ElevenlabsService {

    private final ElevenlabsMapper elevenlabsMapper;
    private final VoiceMapper voiceMapper;
    private final AiVoiceRepository aiVoiceRepository;

    public List<VoiceDto> getAllVoices() {
        return voiceMapper.mapToDto(aiVoiceRepository.findAll());
    }

    public void tts() {
        Voice voice = getVoiceById("21m00Tcm4TlvDq8ikWAM");
        File file = voice.generate("Pozdrawiam Polaka");
        int i = 4;
    }

    public Voice getVoiceById(String id) {
        return Voice.getVoice(id);
    }
}
