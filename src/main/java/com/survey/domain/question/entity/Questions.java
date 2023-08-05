package com.survey.domain.question.entity;

import com.survey.domain.survey.entity.Survey;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private Long surveyId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;



    public void updateQuestion(String title, String type) {
        if(title != null) {
            this.title = title;
        }
        if(type != null) {
            this.questionType = QuestionType.getQuestionType(type);
        }
    }
}
