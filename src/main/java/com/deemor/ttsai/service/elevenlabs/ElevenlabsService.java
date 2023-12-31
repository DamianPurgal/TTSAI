package com.deemor.ttsai.service.elevenlabs;

import com.deemor.ttsai.dto.VoiceDto;
import com.deemor.ttsai.entity.voice.AiVoice;
import com.deemor.ttsai.exception.elevenlabs.ElevenLabsConnectionException;
import com.deemor.ttsai.exception.elevenlabs.ElevenLabsVoiceNotFoundException;
import com.deemor.ttsai.mapper.VoiceMapper;
import com.deemor.ttsai.repository.AiVoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ElevenlabsService {

    private final VoiceMapper voiceMapper;
    private final AiVoiceRepository aiVoiceRepository;

    public List<VoiceDto> getAllVoices() {
        return voiceMapper.mapToDto(aiVoiceRepository.findAll());
    }

    public Voice getVoiceById(String id) {
        try {
            return Voice.getVoice(id);
        } catch (Exception exception) {
            throw new ElevenLabsVoiceNotFoundException();
        }
    }

    public InputStream generateAudioFile(String message, String voiceId) {
        Voice voice = getVoiceById(voiceId);

        try {
            return voice.generateStream(message, "eleven_multilingual_v2");
        } catch (Exception exception) {
            log.warn("ElevenLabs connection error [generate audio file]");
            throw new ElevenLabsConnectionException();
        }
    }

    public InputStream generateAudioFile(String message, Voice voice) {
        try {
            return voice.generateStream(message, "eleven_multilingual_v2");
        } catch (Exception exception) {
            log.warn("ElevenLabs connection error [generate audio file]");
            throw new ElevenLabsConnectionException();
        }
    }

    public Set<Voice> getVoices(Set<String> voiceIds) {
        Set<Voice> voices = new HashSet<>();

        try {
            voiceIds.forEach(voiceId -> voices.add(Voice.getVoice(voiceId)));
            return voices;
        } catch (Exception exception) {
            log.warn("ElevenLabs connection error [generate audio file]");
            throw new ElevenLabsConnectionException();
        }
    }

}
