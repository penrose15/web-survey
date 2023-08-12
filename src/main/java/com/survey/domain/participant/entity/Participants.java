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

    public void updateParticipantInfo(String name, String email) {
        if(name != null) {
            this.name = name;
        }
        if(email != null) {
            this.email = email;
        }
    }
}
