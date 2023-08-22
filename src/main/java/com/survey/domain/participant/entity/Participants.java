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
    void setStatus() {
        this.status = SurveyStatus.NOT_FINISHED;
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

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;
    // 선착순 몇번째?
    @Column
    private Integer number;

    @Builder
    public Participants(Long id, String name, String email, Long surveyId, SurveyStatus status, Integer number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surveyId = surveyId;
        this.status = status;
        this.number = number;
    }


    public void checkParticipantSurveyHasDone() {
        if(this.status == SurveyStatus.NOT_IN_FCFS || this.status == SurveyStatus.IN_FCFS) throw new IllegalStateException("설문조사를 마친 사용자는 변경 불가능");

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

    public void changeParticipantStatus(SurveyStatus status) {
        checkParticipantSurveyHasDone();
        this.status = status;
    }

    public void setNumber(Integer number) {
        if(number != null) {
            if(this.number == null) {
                this.number = number;
            } else {
                throw new IllegalArgumentException("이미 참가하였으면 다시 참가 불가");
            }
        }
    }
}
