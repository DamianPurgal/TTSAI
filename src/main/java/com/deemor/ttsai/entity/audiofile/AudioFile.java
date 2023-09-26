package com.deemor.ttsai.entity.audiofile;

import com.deemor.ttsai.entity.alert.Alert;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "AUDIO_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudioFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "FILE")
    private byte[] audioFile;

    @OneToOne(mappedBy = "audioFile")
    private Alert alert;
}
