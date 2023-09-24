package com.survey.domain.options.repository;

import com.survey.domain.options.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OptionsRepository extends JpaRepository<Options, Long> {

    @Query("SELECT o " +
            "FROM Options o " +
            "WHERE o.questionId = :questionId ")
    List<Options> findAllByQuestionId(Long questionId);

    @Query("SELECT o " +
            "FROM Options o " +
            "WHERE o.questionId IN :questionIds")
    List<Options> findOptionsList(List<Long> questionIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM Options o " +
            "where o.questionId in :questionIds")
    void deleteByQuestionIds(List<Long> questionIds);

    @Transactional
    @Modifying
    @Query("DELETE FROM Options o " +
            "where o.questionId in :questionIds")
    void deleteBySurveyId(List<Long> questionIds);
}
