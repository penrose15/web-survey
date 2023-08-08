package com.survey.domain.question.repository;

import com.survey.domain.question.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Questions, Long> {

    @Query("SELECT q FROM Questions q " +
            "WHERE q.surveyId = :surveyId ")
    List<Questions> findListBySurveyId(Long surveyId);

    @Query("SELECT q FROM Questions q " +
            "WHERE q.surveyId = :surveyId ")
    Page<Questions> findPageBySurveyId(Pageable pageable , Long surveyId);



}
