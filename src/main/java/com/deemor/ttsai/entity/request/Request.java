package com.deemor.ttsai.entity.request;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REQUEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

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
    private RequestStatus requestStatus;

    @Column(name = "DATE_OF_CREATION")
    private LocalDateTime dateOfCreation;

}
