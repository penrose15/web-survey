package com.survey.domain.participant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participants {

    @PrePersist
    void setSurveyDone() {
        this.surveyDone = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long surveyId;

    @Column(nullable = false)
    private boolean surveyDone;

    // 선착순 몇번째?
    @Column
    private Integer number;

    @Builder
    public Participants(Long id, String name, String email, Long surveyId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surveyId = surveyId;
    }

    public void checkParticipantSurveyHasDone() {
        if(this.surveyDone) throw new IllegalStateException("설문조사를 마친 사용자는 변경 불가능");
    }

    public void updateParticipantInfo(String name, String email) {
        checkParticipantSurveyHasDone();

        if(name != null) {
            this.name = name;
        }
        if(email != null) {
            this.email = email;
        }
    }

    public void changeParticipantStatus() {
        checkParticipantSurveyHasDone();
        this.surveyDone = true;
    }
}
