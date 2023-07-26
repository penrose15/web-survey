package com.survey.domain.survey.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResponsesDTO {
    private Long id;
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private Integer userLimit;

    @Builder
    public SurveyResponsesDTO(Long id, String title, String description, String startAt, String endAt, Integer userLimit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.userLimit = userLimit;
    }
}
