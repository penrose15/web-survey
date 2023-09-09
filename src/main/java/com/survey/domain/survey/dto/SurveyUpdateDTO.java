package com.survey.domain.survey.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyUpdateDTO {
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private Integer userLimit;
    private Long categoryId;


}
