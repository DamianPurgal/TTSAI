package com.deemor.ttsai.entity.voice;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "VOICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "VOICE_NAME")
    private String name;

    @Column(name = "VOICE_ID")
    private String voiceId;

    @Column(name = "CATEGORY")
    private String category;


}
