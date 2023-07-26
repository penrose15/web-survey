package com.survey.domain.survey.dto;

import lombok.Getter;

@Getter
public class SurveyUpdateDTO {
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private Integer userLimit;
    private Long categoryId;


}
