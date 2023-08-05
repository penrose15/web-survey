package com.survey.domain.options.repository;

import com.survey.domain.options.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OptionRepository extends JpaRepository<Options, Long> {

    @Query("SELECT o " +
            "FROM Options " +
            "WHERE o.questionId = :questionId " +
            "ORDER BY o.sequence ASC")
    List<Options> findAllByQuestionId(Long questionId);
}
