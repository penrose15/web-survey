package com.survey.domain.respondent.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.global.TestConfig;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.repository.ParticipantsRepository;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.respondent.dto.RespondentEssayDto;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import com.survey.domain.user.entity.Roles;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
public class RespondentRepositoryTest {
    @Autowired
    private EntityManager em;

    private JPAQueryFactory queryFactory;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private RespondentRepositoryImpl respondentRepositoryImpl;

    @Autowired
    private RespondentRepository respondentRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("RespondentRepositoryImpl의 findAnswerByTypeIsEssayAndSurveyId test")
    @Test
    void test() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("password")
                .role(Roles.USER)
                .build();
        user = userRepository.save(user);

        Survey survey = Survey.builder()
                .title("survey title")
                .startAt(LocalDateTime.of(2023,9,9,12,0))
                .endAt(LocalDateTime.of(2023,9,10,12,0))
                .userLimit(10)
                .user(user)
                .build();
        survey = surveyRepository.save(survey);

        List<Questions> questionsList = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            Questions questions = Questions.builder()
                    .title("title "+ i)
                    .surveyId(survey.getId())
                    .sequence(i+1)
                    .questionType(QuestionType.ESSAY)
                    .build();
            questions = questionsRepository.save(questions);
            questionsList.add(questions);
        }

        Participants participants1 = Participants.builder()
                .name("test participant")
                .email("hello@gmail.com")
                .surveyId(survey.getId())
                .build();
        Participants participants2 = Participants.builder()
                .name("test participant")
                .email("hello@gmail.com")
                .surveyId(survey.getId())
                .build();
        participants1 = participantsRepository.save(participants1);
        participants2 = participantsRepository.save(participants2);

        List<Respondent> respondentList = new ArrayList<>();
        for(int i = 0; i<questionsList.size(); i++) {
            Questions questions = questionsList.get(i);

            Respondent respondent = Respondent.builder()
                    .participantsId(participants1.getId())
                    .questionId(questions.getId())
                    .surveyId(survey.getId())
                    .answer("answer " + i)
                    .build();
            respondentList.add(respondent);

            if(i == questionsList.size()-1) {
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .answer("answer by participant2")
                        .build();
                respondentList.add(respondent2);
            }
        }
        respondentList = respondentRepository.saveAll(respondentList);

        List<RespondentEssayDto> list =  respondentRepositoryImpl.findAnswerByTypeIsEssayAndSurveyId(survey.getId());
        for (RespondentEssayDto respondentEssayDto : list) {
            System.out.println(respondentEssayDto);
        }

        Map<Long, List<String>> map = list.stream()
                .collect(Collectors.groupingBy(RespondentEssayDto::getQuestionId,
                        Collectors.mapping(RespondentEssayDto::getAnswer, Collectors.toList())));

        for (Long aLong : map.keySet()) {
            System.out.println("questionId : " + aLong);
            List<String> answers = map.get(aLong);
            System.out.println(answers);
            System.out.println("==============");
            System.out.println();
        }

    }

    @Test
    void deleteTest() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("password")
                .role(Roles.USER)
                .build();
        user = userRepository.save(user);

        Survey survey = Survey.builder()
                .title("survey title")
                .startAt(LocalDateTime.of(2023,9,9,12,0))
                .endAt(LocalDateTime.of(2023,9,10,12,0))
                .userLimit(10)
                .user(user)
                .build();
        survey = surveyRepository.save(survey);

        List<Questions> questionsList = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            Questions questions = Questions.builder()
                    .title("title "+ i)
                    .surveyId(survey.getId())
                    .sequence(i+1)
                    .questionType(QuestionType.ESSAY)
                    .build();
            questions = questionsRepository.save(questions);
            questionsList.add(questions);
        }

        Participants participants1 = Participants.builder()
                .name("test participant")
                .email("hello@gmail.com")
                .surveyId(survey.getId())
                .build();
        Participants participants2 = Participants.builder()
                .name("test participant")
                .email("hello@gmail.com")
                .surveyId(survey.getId())
                .build();
        participants1 = participantsRepository.save(participants1);
        participants2 = participantsRepository.save(participants2);

        List<Respondent> respondentList = new ArrayList<>();
        for(int i = 0; i<questionsList.size(); i++) {
            Questions questions = questionsList.get(i);

            Respondent respondent = Respondent.builder()
                    .participantsId(participants1.getId())
                    .questionId(questions.getId())
                    .surveyId(survey.getId())
                    .answer("answer " + i)
                    .build();
            respondentList.add(respondent);

            if(i == questionsList.size()-1) {
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .answer("answer by participant2")
                        .build();
                respondentList.add(respondent2);
            }
        }
        respondentList = respondentRepository.saveAll(respondentList);

        System.out.println(respondentList.size());

        respondentRepository.deleteAllBySurveyIdAndParticipantId(survey.getId(), participants1.getId());

        List<Respondent> list = respondentRepository.findAll();

        assertThat(list.size())
                .isEqualTo(1);
    }
}
