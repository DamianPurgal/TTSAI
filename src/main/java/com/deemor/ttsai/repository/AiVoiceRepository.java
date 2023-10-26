package com.deemor.ttsai.repository;

import com.deemor.ttsai.entity.voice.AiVoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AiVoiceRepository extends JpaRepository<AiVoice, Long> {
    Optional<AiVoice> findAiVoiceByVoiceId(String voiceId);
    Optional<AiVoice> findFirstByName(String name);
    Set<AiVoice> findAllByNameIn(Set<String> names);

}
