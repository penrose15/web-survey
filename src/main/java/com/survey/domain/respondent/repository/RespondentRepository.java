package com.survey.domain.respondent.repository;

import com.survey.domain.respondent.entity.Respondent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RespondentRepository extends JpaRepository<Respondent, Long> {

    @Query("select count(r.id) from Respondent r " +
            "where r.surveyId = :surveyId")
    int countRespondent(Long surveyId);

    @Query("select r from Respondent r " +
            "where r.participantsId = :participantsId " +
                "and r.surveyId = :surveyId ")
    Page<Respondent> findByParticipantsId(@Param(value = "participantsId") Long participantsId,
                                          @Param("surveyId") Long surveyId,
                                          Pageable pageable);
    @Query("select r from Respondent r " +
            "where r.participantsId = :participantsId ")
    List<Respondent> findListByParticipantsId(@Param(value = "participantsId") Long participantsId);

    @Transactional
    @Modifying
    @Query("delete from Respondent r" +
            " where r.surveyId = :surveyId " +
            "AND r.participantsId = :participantsId")
    void deleteAllBySurveyIdAndParticipantId(@Param("surveyId")Long surveyId, @Param("participantsId")Long participantsId);

    @Transactional
    @Modifying
    @Query("delete from Respondent r" +
            " where r.surveyId = :surveyId ")
    void deleteAllBySurveyId(@Param("surveyId")Long surveyId);
}
