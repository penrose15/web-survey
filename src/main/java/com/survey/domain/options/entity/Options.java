package com.survey.domain.options.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "options")
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String option;

    private Integer sequence;

    private Long questionId;

    @Builder
    public Options(Long id, String option, Integer sequence, Long questionId) {
        this.id = id;
        this.option = option;
        this.sequence = sequence;
        this.questionId = questionId;
    }


}
