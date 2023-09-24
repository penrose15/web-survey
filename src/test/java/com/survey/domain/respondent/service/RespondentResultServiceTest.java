package com.survey.domain.respondent.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.domain.options.dto.FiveMultipleChoiceStatisticDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.entity.SurveyStatus;
import com.survey.domain.participant.repository.ParticipantsRepository;
import com.survey.domain.question.dto.QuestionAndOptionStatisticDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.respondent.dto.RespondentEssayDto;
import com.survey.domain.respondent.dto.RespondentStatisticFormDto;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentRepository;
import com.survey.domain.respondent.repository.RespondentRepositoryImpl;
import com.survey.domain.respondent.service.RespondentResultService;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import com.survey.domain.user.entity.Roles;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@ActiveProfiles("test")
@SpringBootTest
public class RespondentResultServiceTest {
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
    private RespondentRepository respondentRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RespondentResultService respondentResultService;

    private Survey survey;


    @BeforeEach
    void init() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("password")
                .role(Roles.USER)
                .build();
        user = userRepository.save(user);

        survey = Survey.builder()
                .title("survey title")
                .startAt(LocalDateTime.of(2023,9,9,12,0))
                .endAt(LocalDateTime.of(2023,9,10,12,0))
                .userLimit(10)
                .user(user)
                .build();
        survey = surveyRepository.save(survey);

        List<Questions> questionsList = new ArrayList<>();
        for(int i = 0; i<3; i++) {
            QuestionType questionType;
            if(i % 2 == 0) questionType = QuestionType.FIVE_MULTIPLE_CHOICE;
            else questionType = QuestionType.ESSAY;

            Questions questions = Questions.builder()
                    .title("title "+ i)
                    .surveyId(survey.getId())
                    .sequence(i+1)
                    .questionType(questionType)
                    .build();
            questions = questionsRepository.save(questions);
            questionsList.add(questions);
        }

        for(int i = 0; i<questionsList.size(); i++) {
            Questions questions = questionsList.get(i);
            if(i % 2 == 0) {
                for(int j = 0; j<5;j++) {
                    Options options = Options.builder()
                            .choice("option " + (j+1))
                            .sequence(j+1)
                            .questionId(questions.getId())
                            .build();
                    optionsRepository.save(options);
                }
            }
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

        for(int i = 0; i<questionsList.size(); i++) {
            Questions questions = questionsList.get(i);

            List<Options> optionsList = optionsRepository.findAllByQuestionId(questions.getId());
            int random1 = (int) (Math.random() * 4);
            int random2 = (int) (Math.random() * 4);

            if(i % 2 == 0) {
                Options options1 = optionsList.get(random1);
                Options options2 = optionsList.get(random2);

                Respondent respondent1 = Respondent.builder()
                        .participantsId(participants1.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .optionId(options1.getId())
                        .optionSequence(options1.getSequence())
                        .build();
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .optionId(options2.getId())
                        .optionSequence(options2.getSequence())
                        .build();
                respondentRepository.save(respondent1);
                respondentRepository.save(respondent2);
            } else {
                Respondent respondent1 = Respondent.builder()
                        .participantsId(participants1.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .answer("answer1 " + i)
                        .build();
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .answer("answer2 " + i)
                        .build();
                respondentRepository.save(respondent1);
                respondentRepository.save(respondent2);
            }
        }
    }

    @Test
    void getQuestionAndOptionStatisticBySurveyTest() {

        List<QuestionAndOptionStatisticDto> response = respondentResultService.getQuestionAndOptionStatisticBySurvey(survey.getId());
        System.out.println("response size : " + response.size());
        for (QuestionAndOptionStatisticDto questionAndOptionStatisticDto : response) {
            System.out.println("question : \n" + questionAndOptionStatisticDto.getQuestion());
            if(questionAndOptionStatisticDto.getFiveMultipleChoiceStatistics() != null) {
                for (FiveMultipleChoiceStatisticDto fiveMultipleChoiceStatistic : questionAndOptionStatisticDto.getFiveMultipleChoiceStatistics()) {
                    System.out.println("fiveMultipleChoiceStatistic : \n" + fiveMultipleChoiceStatistic);
                }
            }
            System.out.println("essayAnswer : \n"+ questionAndOptionStatisticDto.getEssayStatistics());
        }
    }

    @Test
    void getStatisticsBySurveyIdTest() {
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
            QuestionType questionType;
            if(i % 2 == 0) questionType = QuestionType.FIVE_MULTIPLE_CHOICE;
            else questionType = QuestionType.ESSAY;

            Questions questions = Questions.builder()
                    .title("title "+ i)
                    .surveyId(survey.getId())
                    .sequence(i+1)
                    .questionType(questionType)
                    .build();
            questions = questionsRepository.save(questions);
            questionsList.add(questions);
        }

        for(int i = 0; i<questionsList.size(); i++) {
            Questions questions = questionsList.get(i);
            if(i % 2 == 0) {
                for(int j = 0; j<5;j++) {
                    Options options = Options.builder()
                            .choice("option " + (j+1))
                            .sequence(j+1)
                            .questionId(questions.getId())
                            .build();
                    optionsRepository.save(options);
                }
            }
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

            List<Options> optionsList = optionsRepository.findAllByQuestionId(questions.getId());
            int random1 = (int) (Math.random() * 4);
            int random2 = (int) (Math.random() * 4);

            if(i % 2 == 0) {
                Options options1 = optionsList.get(random1);
                Options options2 = optionsList.get(random2);

                Respondent respondent1 = Respondent.builder()
                        .participantsId(participants1.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .optionId(options1.getId())
                        .optionSequence(options1.getSequence())
                        .build();
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .optionId(options2.getId())
                        .optionSequence(options2.getSequence())
                        .build();
                respondentRepository.save(respondent1);
                respondentRepository.save(respondent2);
            } else {
                Respondent respondent1 = Respondent.builder()
                        .participantsId(participants1.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .answer("answer1 " + i)
                        .build();
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .answer("answer2 " + i)
                        .build();
                respondentRepository.save(respondent1);
                respondentRepository.save(respondent2);
            }
        }
        RespondentStatisticFormDto result = respondentResultService.getStatisticsBySurveyId(survey.getId());

        System.out.println(result);
    }
}
