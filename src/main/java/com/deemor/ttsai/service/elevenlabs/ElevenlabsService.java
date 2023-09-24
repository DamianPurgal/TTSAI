package com.deemor.ttsai.service.elevenlabs;

import com.deemor.ttsai.dto.VoiceDto;
import com.deemor.ttsai.mapper.ElevenlabsMapper;
import lombok.AllArgsConstructor;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@AllArgsConstructor
public class ElevenlabsService {

    private final ElevenlabsMapper elevenlabsMapper;

    public List<VoiceDto> getAllVoices() {
        List<Voice> voices = Voice.getVoices();
        return elevenlabsMapper.mapListVoiceToDto(voices);
    }

    public void tts() {
        Voice voice = getVoiceById("21m00Tcm4TlvDq8ikWAM");
        File file = voice.generate("Pozdrawiam Polaka");
        int i = 4;
    }

    private Voice getVoiceById(String id) {
        return Voice.getVoice(id);
    }
}
