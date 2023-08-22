package com.survey.domain.participant.entity;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum SurveyStatus {
    NOT_FINISHED("not finished yet"),
    FINISHED("survey finished"),
    IN_FCFS("in FCFS"),
    NOT_IN_FCFS("not in FCFS");

    private String value;

    SurveyStatus(String value) {
        this.value = value;
    }

    public static SurveyStatus getStatus(String status) {
        return Arrays.stream(SurveyStatus.values())
                .filter(s -> s.getValue().equals(status))
                .findAny()
                .orElse(NOT_FINISHED);
    }
}
