package com.survey.domain.respondent.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Respondent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private Long surveyId;

    @Column(nullable = false)
    private Long questionId;

    private Long optionId;

    @Column(nullable = false)
    private Long participantsId;

    @Builder
    public Respondent(Long id, String answer, Long surveyId, Long questionId, Long optionId, Long participantsId) {
        this.id = id;
        this.answer = answer;
        this.surveyId = surveyId;
        this.questionId = questionId;
        this.optionId = optionId;
        this.participantsId = participantsId;
    }
}
