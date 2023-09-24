package com.survey.domain.respondent.repository;

import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentJdbcRepository;
import com.survey.domain.respondent.repository.RespondentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
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
        long beforeTime = System.currentTimeMillis();
        respondentJdbcRepository.saveAll(list);
        long afterTime = System.currentTimeMillis();
        int respondentCount = respondentRepository.countRespondent(surveyId);

        System.out.println("# 시간차 : " + (afterTime - beforeTime));
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
        long beforeTime = System.currentTimeMillis();
        respondentRepository.saveAll(list);
        long afterTime = System.currentTimeMillis();

        int respondentCount = respondentRepository.countRespondent(surveyId);

        System.out.println("# 시간차 : " + (afterTime - beforeTime));
        assertThat(respondentCount)
                .isEqualTo(N);
    }

    @Test
    void updateJdbcTest() {
        int N = 100;
        Long surveyId = 1L;
        List<Respondent> list = new ArrayList<>();
        for(int i = 1; i<=N; i++) {
            Respondent respondent = Respondent.builder()
                    .optionId(null)
                    .surveyId(surveyId)
                    .participantsId(1L)
                    .questionId((long)i)
                    .answer("answer "+i)
                    .build();
            list.add(respondent);
        }
        respondentJdbcRepository.saveAll(list);

        List<Respondent> respondents = respondentRepository.findAll();
        List<Respondent> updateList = new ArrayList<>();
        for(int i = 1; i<=N; i++) {
            Respondent respondent = respondents.get(i-1);
            respondent.updateRespondent("update answer " + i, null, null);

            updateList.add(respondent);
        }

        respondentJdbcRepository.updateAll(updateList);

        List<Respondent> results = respondentRepository.findAll();

        for (Respondent result : results) {
            assertThat(result.getAnswer())
                    .contains("update");
        }
    }
}
