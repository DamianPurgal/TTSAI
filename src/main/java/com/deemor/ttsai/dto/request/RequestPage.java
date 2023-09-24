package com.deemor.ttsai.dto.request;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPage {
    private List<RequestDto> requests;
    private Integer totalNumberOfPages;
    private Integer totalNumberOfElements;
}
