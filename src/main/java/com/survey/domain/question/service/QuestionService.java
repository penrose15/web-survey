package com.survey.domain.question.service;

import com.survey.domain.options.repository.OptionRepository;
import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionRepository;
import com.survey.domain.survey.service.SurveyFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final SurveyFindService surveyFindService;

    public Questions createQuestion(QuestionRequestDto questionRequestDto, Long surveyId, String email) {

        Questions questions = Questions.builder()
                .title(questionRequestDto.getTitle())
                .surveyId(surveyId)
                .questionType(QuestionType.getQuestionType(questionRequestDto.getQuestionType()))
                .build();

        questions = questionRepository.save(questions);
        return questions;
    }

    public Long updateQuestion(QuestionRequestDto questionRequestDto, Long questionId, Long surveyId, String email) {
        surveyFindService.findByIdAndEmail(email, surveyId);
        Questions questions = findById(questionId);
        questions.updateQuestion(questions.getTitle(), questionRequestDto.getQuestionType());

        questions = questionRepository.save(questions);

        return questions.getId();
    }

    public List<Questions> findBySurveyId(Long surveyId) {
        return questionRepository.findListBySurveyId(surveyId);
    }

    public Page<Questions> findPageBySurveyId(Long surveyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return questionRepository.findPageBySurveyId(pageable, surveyId);
    }

    //조회는 options와 같이 보여줘야 할 것 같아서 패스

    public Questions findById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 question"));
    }


    public void deleteById(Long questionId) {
        Questions question = findById(questionId);
        questionRepository.deleteById(questionId);
    }

    public void deleteQuestionByQuestionIdList(List<Long> questionIdList) {
        questionRepository.deleteAllById(questionIdList);
    }

    public int countQuestionsBySurveyId(Long surveyId) {
        return questionRepository.countQuestionsBySurveyId(surveyId);
    }
}
