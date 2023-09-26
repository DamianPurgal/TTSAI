package com.deemor.ttsai.dto.alert;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertPage {
    private List<AlertDto> requests;
    private Integer totalNumberOfPages;
    private Integer totalNumberOfElements;
}
