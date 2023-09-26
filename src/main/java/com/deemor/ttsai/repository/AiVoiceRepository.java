package com.deemor.ttsai.repository;

import com.deemor.ttsai.entity.voice.AiVoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiVoiceRepository extends JpaRepository<AiVoice, Long> {
    Optional<AiVoice> findAiVoiceByVoiceId(String voiceId);
    Optional<AiVoice> findFirstByName(String name);
}
