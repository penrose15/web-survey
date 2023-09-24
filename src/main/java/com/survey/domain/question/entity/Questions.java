package com.survey.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Questions {

    @PrePersist
    void setEssentialIfNull() {
        if(this.isEssential == null) {
            this.isEssential = false;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long surveyId;

    @Column
    private String imageName;

    @Column
    private String imageUrl;

    @Column
    private Integer sequence;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(name = "essential")
    private Boolean isEssential;

    public void updateQuestion(String title, String type, Boolean isEssential) {
        if(title != null) {
            this.title = title;
        }
        if(type != null) {
            this.questionType = QuestionType.getQuestionType(type);
        }
        if(isEssential != null) {
            this.isEssential = isEssential;
        }
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
