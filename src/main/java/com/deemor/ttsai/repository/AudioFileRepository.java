package com.deemor.ttsai.repository;

import com.deemor.ttsai.entity.audiofile.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
    Optional<AudioFile> findFirstByMessageAndVoiceType(String message, String voiceType);

}
