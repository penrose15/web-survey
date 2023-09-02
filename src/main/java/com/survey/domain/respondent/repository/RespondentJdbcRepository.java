package com.survey.domain.respondent.repository;

import com.survey.domain.respondent.entity.Respondent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RespondentJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Respondent> respondents) {
        String sql = "INSERT INTO respondent (option_id, participants_id, question_id, survey_id, answer) " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql,
                respondents,
                respondents.size(),
                (PreparedStatement ps, Respondent respondent) -> {

            if(respondent.getOptionId() == null) {
                ps.setNull(1, Types.BIGINT);
            }
            else {
                ps.setLong(1, respondent.getOptionId());
            }
            if(respondent.getParticipantsId() == null) {
                throw new RuntimeException("participantsId cannot be null");
            }
            else {
                ps.setLong(2, respondent.getParticipantsId());
            }
            if(respondent.getQuestionId() == null) {
                throw new RuntimeException("questionId cannot be null");
            }
            else {
                ps.setLong(3, respondent.getQuestionId());
            }
            if(respondent.getSurveyId() == null) {
                throw new RuntimeException("surveyId cannot be null");
            }
            else {
                ps.setLong(4, respondent.getSurveyId());
            }
            if(respondent.getAnswer() == null) {
                ps.setNull(5, Types.VARCHAR);
            }
            else {
                ps.setString(5, respondent.getAnswer());
            }
                });
    }

    @Transactional
    public void updateAll(List<Respondent> respondents) {
        String sql = "UPDATE respondent SET answer = ?, option_id = ?, option_sequence = ? where id = ?";
        jdbcTemplate.batchUpdate(sql,
                respondents,
                respondents.size(),
                (PreparedStatement ps, Respondent respondent) -> {
                    if(respondent.getAnswer() != null) {
                        ps.setString(1, respondent.getAnswer());
                    } else {
                        ps.setNull(1, Types.VARCHAR);
                    }
                    if(respondent.getOptionId() != null) {
                        ps.setLong(2, respondent.getOptionId());
                    } else {
                        ps.setNull(2, Types.BIGINT);
                    }
                    if(respondent.getOptionSequence() != null) {
                        ps.setInt(3, respondent.getOptionSequence());
                    } else {
                        ps.setNull(3, Types.INTEGER);
                    }
                    ps.setLong(4, respondent.getId());
                });
    }
}
