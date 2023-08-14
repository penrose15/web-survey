package com.survey.respondent.repository;

import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentJdbcRepository;
import com.survey.domain.respondent.repository.RespondentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RespondentJDBCRepositoryTest {

    @Autowired
    RespondentJdbcRepository respondentJdbcRepository;

    @Autowired
    RespondentRepository respondentRepository;

    @Test
    @DisplayName("jdbc 작동 잘 되는지 테스트")
    void insertTestWithJdbcTemplate() {
        int N = 100;
        Long surveyId = 1L;
        List<Respondent> list = new ArrayList<>();
        for(int i = 1; i<=N; i++) {
            Respondent respondent = Respondent.builder()
                    .optionId(null)
                    .surveyId(surveyId)
                    .participantsId((long)i)
                    .questionId((long)i)
                    .answer("answer "+i)
                    .build();
            list.add(respondent);
        }
        respondentJdbcRepository.saveAll(list);

        int respondentCount = respondentRepository.countRespondent(surveyId);

        assertThat(respondentCount)
                .isEqualTo(N);
    }

    @Test
    @DisplayName("비교 테스트")
    void insertTestWithJpa() {
        int N = 100;
        Long surveyId = 1L;
        List<Respondent> list = new ArrayList<>();
        for(int i = 1; i<=N; i++) {
            Respondent respondent = Respondent.builder()
                    .optionId(null)
                    .surveyId(surveyId)
                    .participantsId((long)i)
                    .questionId((long)i)
                    .answer("answer "+i)
                    .build();
            list.add(respondent);
        }
        respondentRepository.saveAll(list);

        int respondentCount = respondentRepository.countRespondent(surveyId);

        assertThat(respondentCount)
                .isEqualTo(N);
    }
}
