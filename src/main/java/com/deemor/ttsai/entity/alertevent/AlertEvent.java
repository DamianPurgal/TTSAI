package com.deemor.ttsai.entity.alertevent;

import com.deemor.ttsai.entity.alert.Alert;
import com.deemor.ttsai.entity.audiofile.AudioFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ALERT_EVENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ORDER_INDEX")
    private Long orderIndex;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ALERT_ID", referencedColumnName = "id")
    private Alert alert;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AUDIO_FILE_ID", referencedColumnName = "id")
    private AudioFile audioFile;

}
