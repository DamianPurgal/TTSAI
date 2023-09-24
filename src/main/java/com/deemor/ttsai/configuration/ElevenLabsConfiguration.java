package com.deemor.ttsai.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.andrewcpu.elevenlabs.ElevenLabs;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "application.elevenlabs")
public class ElevenLabsConfiguration {

    private String apiKey;

    @PostConstruct
    public void init() {
        ElevenLabs.setApiKey(apiKey);
        List<Voice> voices = Voice.getVoices();

    }
}
