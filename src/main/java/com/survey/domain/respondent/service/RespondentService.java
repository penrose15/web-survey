package com.survey.domain.respondent.service;

import com.survey.domain.participant.entity.Participants;
import com.survey.domain.participant.entity.SurveyStatus;
import com.survey.domain.participant.service.ParticipantsService;
import com.survey.domain.question.service.QuestionService;
import com.survey.domain.respondent.dto.RespondentRequestDto;
import com.survey.domain.respondent.dto.RespondentUpdateDto;
import com.survey.domain.respondent.entity.Respondent;
import com.survey.domain.respondent.repository.RespondentJdbcRepository;
import com.survey.domain.respondent.repository.RespondentRepository;
import com.survey.domain.survey.entity.Survey;
import com.survey.domain.survey.service.SurveyFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public void createRespondents(List<RespondentRequestDto> requests, Long surveyId, Long participantId) {
        Survey survey = surveyFindService.findSurveyById(surveyId);
        int count = questionService.countQuestionsBySurveyId(surveyId);

        if(count != requests.size()) throw new IllegalStateException("모든 질문에 답변해주세요");

        List<Respondent> respondents = new ArrayList<>();
        for (RespondentRequestDto request : requests) {
            //서술형인 경우 options가 null이므로
            String answer = request.getAnswer() != null ? request.getAnswer() : null;
            Long optionId = request.getOptionId() != null ? request.getOptionId() : null;
            Integer optionSequence = request.getOptionSequence() != null ? request.getOptionSequence() : null;

            Respondent respondent = Respondent.builder()
                    .answer(answer)
                    .optionId(optionId)
                    .participantsId(participantId)
                    .surveyId(surveyId)
                    .optionSequence(optionSequence)
                    .build();
            respondents.add(respondent);
        }

        // userLimit를 정하지 않았다면 그냥 저장
        if(survey.getUserLimit() != -1) {
            /*
              survey에 이미 참가한 participants 수를 센다.
              만약 participants 의 숫자가 survey의 userlimit 보다 적다면
              respondents를 저장하고 participant의 status IN_FCFS로 변경, number를 survey에 참여한 참여자 수 + 1로 저장한다 (아니면 redis로 +1 씩 한 숫자로 설정하든가)

              만약 userlimit보다 크다면 -> 일단 저장하고 status NOT_IN_FCFS로 변경
             */
            int limit = survey.getUserLimit();
            if(limit > participantsService.participantCount(surveyId)) {
                int sequence = participantsService.participantCount(surveyId) + 1;
                participantsService.changeParticipantStatus(participantId, sequence, SurveyStatus.IN_FCFS);
            }
            else {
                int sequence = participantsService.participantCount(surveyId) + 1;
                participantsService.changeParticipantStatus(participantId, sequence, SurveyStatus.NOT_IN_FCFS);
            }
            respondentJdbcRepository.saveAll(respondents);
        }
        else {
            respondentJdbcRepository.saveAll(respondents);
            participantsService.changeParticipantStatus(participantId, null, SurveyStatus.FINISHED);
        }
    }

    public void updateRespondent(List<RespondentUpdateDto> request, Long surveyId, Long participantId) {
        Survey survey = surveyFindService.findSurveyById(surveyId);
        if(survey.getUserLimit() != -1) {
            throw new IllegalArgumentException("선착순 설문조사인 경우 질문 수정이 불가능합니다.");
        }

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

    public void deleteRespondent(Long surveyId, Long participantId) {
        respondentRepository.deleteAllBySurveyIdAndParticipantId(surveyId, participantId);
        Participants participants = participantsService.getParticipants(participantId);

        participants.changeParticipantStatus(SurveyStatus.DELETED);
        participantsService.updateParticipants(participants);
        log.info("respondents deleted");
    }



}
