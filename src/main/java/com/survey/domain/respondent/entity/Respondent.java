package com.survey.domain.respondent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private String respond;

    @Column(nullable = false)
    private Long questionId;

    private Long optionId;

    private Long participantsId;

    @Builder
    public Respondent(Long id, String respond, Long questionId, Long optionId, Long participantsId) {
        this.id = id;
        this.respond = respond;
        this.questionId = questionId;
        this.optionId = optionId;
        this.participantsId = participantsId;
    }
}
