package com.survey.global.redis;

import com.survey.domain.options.entity.Options;
import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.entity.SurveyStatus;
import com.survey.domain.participant.repository.ParticipantsRepository;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.respondent.repository.RespondentRepository;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.repository.SurveyRepository;
import com.survey.domain.user.entity.Roles;
import com.survey.domain.user.entity.User;
import com.survey.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class FcfsServiceTest {
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
    private UserCountRepository countRepository;
    @Autowired
    private FcfsService fcfsService;

    private User user;
    private Survey survey;
    private List<Questions> questionsList;
    private List<Participants> participants;
    int userLimit = 100;
    int N = 120;

    String key;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("test@gmail.com")
                .password("password")
                .role(Roles.USER)
                .build();
        user = userRepository.save(user);

        survey = Survey.builder()
                .title("survey title")
                .startAt(LocalDateTime.of(2023,9,9,12,0))
                .endAt(LocalDateTime.of(2023,9,10,12,0))
                .userLimit(userLimit)
                .user(user)
                .build();
        survey = surveyRepository.save(survey);

        questionsList = new ArrayList<>();
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

        participants = new ArrayList<>();

        for(int i = 0; i<N; i++) {
            Participants participant = Participants.builder()
                    .name("test "+ i)
                    .email("test"+i+"@gmail.com")
                    .surveyId(survey.getId())
                    .status(SurveyStatus.NOT_FINISHED)
                    .build();
            participants.add(participant);
        }
        participantsRepository.saveAll(participants);

        key = String.valueOf(survey.getId());
    }

    @AfterEach
    void clear() {
        countRepository.deleteAllByKey(key);
    }

    @Test
    void fcfsTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(N);
        int limit = survey.getUserLimit();

        for (Participants participant : participants) {
            executorService.submit(() -> {
                try {
                    fcfsService.applyFcfsService(survey.getId(), limit, participant.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        List<Participants> participantsList = participantsRepository.findAll();
        for (Participants participant : participantsList) {
            System.out.println(participant);
        }

        int notInFcfs = N - userLimit;

        int resultNotInFcfs = participantsRepository.countParticipantNotInFCFS(survey.getId());
        int resultInFcfs = participantsRepository.countParticipantInFCFS(survey.getId());

        System.out.println(notInFcfs);
        System.out.println(resultNotInFcfs);
        System.out.println(resultInFcfs);

        assertThat(resultNotInFcfs)
                .isEqualTo(notInFcfs);
        assertThat(resultInFcfs)
                .isEqualTo(userLimit);


    }


}
