package com.deemor.ttsai.repository;

import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.alert.AlertStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertRepository extends PagingAndSortingRepository<Alert, Long> {

    Page<Alert> findAllByAlertStatus(AlertStatus status, Pageable pageable);

    Optional<Alert> findFirstByAlertStatusOrderByDateOfCreationAsc(AlertStatus status);

    Optional<Alert> findFirstByVoiceTypeAndMessage(String voiceType, String message);

}
