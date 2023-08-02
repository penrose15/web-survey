package com.survey.domain.question.entity;

import com.survey.domain.survey.entity.Survey;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Builder
    public Questions(Long id, String title, Survey survey, QuestionType questionType) {
        this.id = id;
        this.title = title;
        this.survey = survey;
        this.questionType = questionType;
    }

    public void updateQuestion(String title, String type) {
        if(title != null) {
            this.title = title;
        }
        if(type != null) {
            this.questionType = QuestionType.getQuestionType(type);
        }
    }
}
