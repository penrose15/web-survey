package com.survey.domain.question.service;

import com.survey.domain.options.dto.OptionsResponseDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.question.dto.QuestionOptionResponseDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.question.service.QuestionAndOptionService;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import com.survey.domain.user.entity.Roles;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class QuestionAndOptionServiceTest {
    @Autowired
    QuestionAndOptionService questionAndOptionService;

    @Autowired
    QuestionsRepository questionsRepository;

    @Autowired
    OptionsRepository optionsRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void getQuestionsAndOptionsBySurveyIdTest() {
        User user = User.builder()
                .email("root@gmail.com")
                .password("password123!")
                .role(Roles.USER)
                .build();
        user = userRepository.save(user);

        Survey survey = Survey.builder()
                .title("survey title")
                .user(user)
                .userLimit(10)
                .endAt(LocalDateTime.now())
                .startAt(LocalDateTime.now().plusDays(1L))
                .build();
        survey = surveyRepository.save(survey);

        List<Long> questionIds = new ArrayList<>();
        for(int i = 1; i<=5; i++) {
            QuestionType questionType;
            if(i % 2 == 0) {
                questionType = QuestionType.FIVE_MULTIPLE_CHOICE;
            } else {
                questionType = QuestionType.ESSAY;
            }

            Questions questions = Questions.builder()
                    .title("question title " + i)
                    .questionType(questionType)
                    .surveyId(survey.getId())
                    .build();
            questions = questionsRepository.save(questions);
            questionIds.add(questions.getId());
        }

        for (Long questionId : questionIds) {
            if(questionId % 2 == 0) {
                for(int i = 0; i<5; i++) {
                    Options options = Options.builder()
                            .questionId(questionId)
                            .option(questionId + " : option "+(i+1))
                            .build();
                    optionsRepository.save(options);
                    System.out.println("### " + questionId);
                }
            }
        }

        System.out.println(questionIds);

        Page<QuestionOptionResponseDto> result = questionAndOptionService.getQuestionsAndOptionsBySurveyId(0, 10, survey.getId());

        List<QuestionOptionResponseDto> resultList = result.getContent();

        for (QuestionOptionResponseDto questionOptionResponseDto : resultList) {
            System.out.println("{");
            System.out.println("id : " + questionOptionResponseDto.getId());
            System.out.println("title : " + questionOptionResponseDto.getTitle());
            System.out.println("questionType : " + questionOptionResponseDto.getQuestionType());
            System.out.println("[");
            for (OptionsResponseDto optionsResponseDto: questionOptionResponseDto.getOptionsList()) {
                System.out.println("{");
                System.out.println("id : " + optionsResponseDto.getId());
                System.out.println("options : " + optionsResponseDto.getOptions());
                System.out.println("}");
            }
            System.out.println("]");
            System.out.println("}");
        }
    }
}
