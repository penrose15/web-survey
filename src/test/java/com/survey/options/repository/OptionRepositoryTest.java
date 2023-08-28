package com.survey.options.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.survey.config.TestConfig;
import com.survey.domain.options.dto.FiveMultipleChoiceStatisticDto;
import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.options.repository.OptionsRepositoryImpl;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.repository.ParticipantsRepository;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.question.repository.QuestionsRepositoryImpl;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentRepository;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DataJpaTest
@Import(TestConfig.class)
public class OptionRepositoryTest {
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
    private OptionsRepositoryImpl optionsRepositoryImpl;

    @Autowired
    private RespondentRepository respondentRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("optionsRepositoryImpl의 findFiveMultipleChoiceStatisticByQuestionIds test")
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
        Map<Long, List<Options>> optionMap = new HashMap<>();
        for(int i = 0; i<3; i++) {
            Questions questions = Questions.builder()
                    .title("title "+ i)
                    .surveyId(survey.getId())
                    .sequence(i+1)
                    .questionType(QuestionType.FIVE_MULTIPLE_CHOICE)
                    .build();
            questions = questionsRepository.save(questions);
            questionsList.add(questions);
            List<Options> optionsList = new ArrayList<>();
            for(int j = 0; j<5; j++) {
                Options options = Options.builder()
                        .questionId(questions.getId())
                        .option("option " + j)
                        .sequence(j+1)
                        .build();
                optionsList.add(options);
            }
            optionsList = optionsRepository.saveAll(optionsList);
            optionMap.put(questions.getId(), optionsList);
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
            List<Options> optionsList = optionMap.get(questionsList.get(i).getId());
            int number = (int) (Math.random()*5);
            Options options = optionsList.get(number);

            Respondent respondent = Respondent.builder()
                    .participantsId(participants1.getId())
                    .questionId(questions.getId())
                    .surveyId(survey.getId())
                    .optionId(options.getId())
                    .optionSequence(options.getSequence())
                    .answer(options.getOption())
                    .build();
            respondentList.add(respondent);

            if(i == questionsList.size()-1) {
                Respondent respondent2 = Respondent.builder()
                        .participantsId(participants2.getId())
                        .questionId(questions.getId())
                        .surveyId(survey.getId())
                        .optionId(options.getId())
                        .optionSequence(options.getSequence())
                        .answer(options.getOption())
                        .build();
                respondentList.add(respondent2);
            }
        }
        respondentList = respondentRepository.saveAll(respondentList);

        List<FiveMultipleChoiceStatisticDto> fiveMultipleChoiceStatisticDtos
                 = optionsRepositoryImpl.findFiveMultipleChoiceStatisticByQuestionIds(survey.getId());

        for (FiveMultipleChoiceStatisticDto fiveMultipleChoiceStatisticDto : fiveMultipleChoiceStatisticDtos) {
            System.out.println(fiveMultipleChoiceStatisticDto);
        }

        Map<Long, List<FiveMultipleChoiceStatisticDto>> map = fiveMultipleChoiceStatisticDtos
                .stream()
                .collect(Collectors.groupingBy(FiveMultipleChoiceStatisticDto::getQuestionId));

        for (Long aLong : map.keySet()) {
            System.out.println("questionId : " + aLong);
            List<FiveMultipleChoiceStatisticDto> list = map.get(aLong);
            for (FiveMultipleChoiceStatisticDto fiveMultipleChoiceStatisticDto : list) {
                System.out.println(fiveMultipleChoiceStatisticDto);
            }
            System.out.println("======================");
            System.out.println();
        }
    }
}
