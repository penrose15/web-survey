package com.survey.domain.respondent.repository;

import com.survey.domain.respondent.entity.Respondent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RespondentRepository extends JpaRepository<Respondent, Long> {

    @Query("select count(r.id) from Respondent r " +
            "where r.surveyId = :surveyId")
    int countRespondent(Long surveyId);
}
