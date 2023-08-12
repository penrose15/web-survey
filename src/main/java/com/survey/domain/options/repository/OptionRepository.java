package com.survey.domain.options.repository;

import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.options.entity.Options;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionRepository extends JpaRepository<Options, Long> {

    @Query("SELECT o " +
            "FROM Options o " +
            "WHERE o.questionId = :questionId ")
    List<Options> findAllByQuestionId(Long questionId);

    @Query("SELECT o " +
            "FROM Options o " +
            "WHERE o.questionId IN :questionIds")
    List<Options> findOptionsList(List<Long> questionIds);
}
