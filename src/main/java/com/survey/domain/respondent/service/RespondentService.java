package com.survey.domain.respondent.service;

import com.survey.domain.participant.entity.SurveyStatus;
import com.survey.domain.participant.service.ParticipantsService;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.service.QuestionService;
import com.survey.domain.respondent.dto.RespondentRequestDto;
import com.survey.domain.respondent.dto.RespondentResponseDto;
import com.survey.domain.respondent.dto.RespondentUpdateDto;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentJdbcRepository;
import com.survey.domain.respondent.repository.RespondentRepository;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.global.redis.FcfsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class RespondentService {

    private final RespondentRepository respondentRepository;
    private final RespondentJdbcRepository respondentJdbcRepository;
    private final QuestionService questionService;
    private final SurveyFindService surveyFindService;
    private final ParticipantsService participantsService;
    private final FcfsService fcfsService;

    public void createRespondents(List<RespondentRequestDto> requests, Long surveyId, Long participantId) {
        Survey survey = surveyFindService.findSurveyById(surveyId);

        checkSurveyPeriod(survey);

        checkEssentialQuestionsAllAnswered(requests, surveyId);

        saveRespondents(requests, participantId, survey);
    }

    private void saveRespondents(List<RespondentRequestDto> requests, Long participantId, Survey survey) {
        Long surveyId = survey.getId();
        List<Respondent> respondents = getResponses(requests, surveyId, participantId);

        if(survey.getUserLimit() > 0) {
            int limit = survey.getUserLimit();
            fcfsService.applyFcfsService(surveyId, limit, participantId);
        }
        else {
            participantsService.changeParticipantStatusAndSequence(participantId, null, SurveyStatus.FINISHED);
        }
        respondentJdbcRepository.saveAll(respondents);
    }

    private void checkEssentialQuestionsAllAnswered(List<RespondentRequestDto> requests, Long surveyId) {
        List<Questions> essentialQuestions = questionService.findBySurveyAndIsEssential(surveyId);
        int essentialQuestionCount = essentialQuestions.size();

        Map<Long, RespondentRequestDto> respondentMap = getResponseMap(requests);

        for (Questions essentialQuestion : essentialQuestions) {
            if(respondentMap.get(essentialQuestion.getId()) != null) {
                RespondentRequestDto request = respondentMap.get(essentialQuestion.getId());
                if(request.getAnswer() != null && request.getOptionId() != null) {
                    essentialQuestionCount -= 1;
                }
            }
        }

        if(essentialQuestionCount != 0) throw new IllegalStateException("모든 질문에 답변해주세요");
    }

    private Map<Long, RespondentRequestDto> getResponseMap(List<RespondentRequestDto> requests) {
        Map<Long, RespondentRequestDto> responseMap = new HashMap<>();
        for (RespondentRequestDto request : requests) {
            responseMap.put(request.getQuestionId(), request);
        }
        return responseMap;
    }

    private List<Respondent> getResponses(List<RespondentRequestDto> requests, Long surveyId, Long participantId) {
        List<Respondent> respondents = new ArrayList<>();
        for (RespondentRequestDto request : requests) {
            Long optionId = request.getOptionId() == null ? null : request.getOptionId();
            String answer = request.getAnswer() == null ? null : request.getAnswer();
            Integer optionSequence = request.getOptionSequence() == null ? null : request.getOptionSequence();

            Respondent respondent = Respondent.builder()
                    .answer(answer)
                    .questionId(request.getQuestionId())
                    .optionId(optionId)
                    .participantsId(participantId)
                    .surveyId(surveyId)
                    .optionSequence(optionSequence)
                    .build();
            respondents.add(respondent);
        }
        return respondents;
    }

    private void checkSurveyPeriod(Survey survey) {
        if(survey.getStartAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("설문조사 시간 전에는 설문조사를 시작할 수 없습니다.");
        }
        if(survey.getEndAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("설문조사 기간에만 설문 작성이 가능합니다.");
        }
    }

    public void updateRespondent(List<RespondentUpdateDto> request, Long surveyId, Long participantId) {
        Survey survey = surveyFindService.findSurveyById(surveyId);
        checkSurvey(survey);

        List<Respondent> respondents = respondentRepository.findListByParticipantsId(participantId);
        Map<Long, RespondentUpdateDto> respondentDtoMap = request.stream()
                .collect(Collectors.toMap(RespondentUpdateDto::getRespondentId, i2 -> i2));

        List<Respondent> updateRespondentList = new ArrayList<>();
        for (Respondent respondent : respondents) {
            RespondentUpdateDto dto = respondentDtoMap.get(respondent.getId());
            respondent.updateRespondent(dto.getAnswer(), dto.getOptionId(), dto.getOptionSequence());
            updateRespondentList.add(respondent);
        }
        respondentJdbcRepository.updateAll(updateRespondentList);

    }

    private void checkSurvey(Survey survey) {
        if(survey.getUserLimit() != -1) {
            throw new IllegalArgumentException("선착순 설문조사인 경우 질문 수정이 불가능합니다.");
        }
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dueDate = survey.getEndAt();

        if(today.isAfter(dueDate)) {
            throw new IllegalStateException("기간이 지난 설문조사는 수정이 불가능합니다.");
        }

    }

    public void deleteRespondent(Long surveyId, Long participantId) {
        respondentRepository.deleteAllBySurveyIdAndParticipantId(surveyId, participantId);
        participantsService.updateParticipantsStatus(participantId, SurveyStatus.DELETED);
        log.info("respondents deleted");
    }

    public void deleteRespondent(Long surveyId) {
        respondentRepository.deleteAllBySurveyId(surveyId);
    }



}
