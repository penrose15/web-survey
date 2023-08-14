package com.survey.domain.respondent.repository;

import com.survey.domain.respondent.entity.Respondent;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
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
            long optionId = respondent.getOptionId() == null ? 0L : respondent.getOptionId();

            ps.setLong(1, optionId);
            ps.setLong(2, respondent.getParticipantsId());
            ps.setLong(3, respondent.getQuestionId());
            ps.setLong(4, respondent.getSurveyId());
            ps.setString(5, respondent.getAnswer());
                });
    }
}
