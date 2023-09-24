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
    private String choice;
    @Column(nullable = false)
    private Long questionId;
    @Column
    private Integer sequence;

    @Builder
    public Options(Long id, String choice, Long questionId, Integer sequence) {
        this.id = id;
        this.choice = choice;
        this.questionId = questionId;
        this.sequence = sequence;
    }


    public void updateOption(OptionsRequestDto dto) {
        if(dto.getChoice() != null) {
            this.choice = dto.getChoice();
        }
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
