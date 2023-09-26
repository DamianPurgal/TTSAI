package com.deemor.ttsai.entity.alert;

import com.deemor.ttsai.entity.audiofile.AudioFile;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ALERT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "VOICE_TYPE")
    private String voiceType;

    @Column(name = "REQUESTER")
    private String requester;

    @Column(name = "SUBMITTER")
    private String submitter;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private AlertStatus alertStatus;

    @Column(name = "DATE_OF_CREATION")
    private LocalDateTime dateOfCreation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AUDIO_FILE_ID", referencedColumnName = "id")
    private AudioFile audioFile;

}