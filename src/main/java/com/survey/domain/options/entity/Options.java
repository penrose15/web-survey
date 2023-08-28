package com.survey.domain.options.entity;

import com.survey.domain.options.dto.OptionsRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "options")
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String option;
    @Column(nullable = false)
    private Long questionId;
    @Column
    private Integer sequence;

    @Builder
    public Options(Long id, String option, Long questionId, Integer sequence) {
        this.id = id;
        this.option = option;
        this.questionId = questionId;
        this.sequence = sequence;
    }


    public void updateOption(OptionsRequestDto dto) {
        if(dto.getOption() != null) {
            this.option = dto.getOption();
        }
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
