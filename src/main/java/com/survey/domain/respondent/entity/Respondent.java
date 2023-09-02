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

    private String answer;

    @Column(nullable = false)
    private Long surveyId;

    @Column(nullable = false)
    private Long questionId;

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "option_sequence")
    private Integer optionSequence;

    @Column(name = "participants_id",nullable = false)
    private Long participantsId;

    @Builder
    public Respondent(Long id, String answer, Long surveyId, Long questionId, Long optionId, Integer optionSequence, Long participantsId) {
        this.id = id;
        this.answer = answer;
        this.surveyId = surveyId;
        this.questionId = questionId;
        this.optionId = optionId;
        this.optionSequence = optionSequence;
        this.participantsId = participantsId;
    }

    public void updateRespondent(String answer, Long optionId, Integer optionSequence) {
        if(answer != null) {
            this.answer = answer;
        }
        if(optionId != null) {
            this.optionId = optionId;
        }
        if(optionSequence != null) {
            this.optionSequence = optionSequence;
        }
    }
}
