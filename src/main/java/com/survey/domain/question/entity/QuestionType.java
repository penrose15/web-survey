package com.survey.domain.question.entity;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum QuestionType {
    FIVE_MULTIPLE_CHOICE("five_multiple_choice"),
    ESSAY("essay");

    private String type;

    QuestionType(String type) {
        this.type = type;
    }

    public static QuestionType getQuestionType(String type) {
        return Arrays.stream(QuestionType.values())
                .filter(q -> q.type.equals(type))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 questionType"));
    }
}
