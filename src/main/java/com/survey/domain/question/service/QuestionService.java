package com.survey.domain.question.service;

import com.survey.domain.options.repository.OptionsRepository;
import com.survey.domain.question.dto.QuestionRequestDto;
import com.survey.domain.question.entity.QuestionType;
import com.survey.domain.question.entity.Questions;
import com.survey.domain.question.repository.QuestionsRepository;
import com.survey.domain.survey.service.SurveyFindService;
import com.survey.global.aws.AwsS3Service;
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
    private final QuestionsRepository questionsRepository;
    private final OptionsRepository optionsRepository;
    private final SurveyFindService surveyFindService;
    private final AwsS3Service awsS3Service;

    public Questions createQuestion(QuestionRequestDto questionRequestDto, Long surveyId, Integer sequence) {
        String filename = awsS3Service.getFileName(questionRequestDto.getMultipartFile());
        String url = awsS3Service.uploadFile(questionRequestDto.getMultipartFile());

        String title = questionRequestDto.getQuestionDto().getTitle();
        String questionType = questionRequestDto.getQuestionDto().getQuestionType();

        Questions questions = Questions.builder()
                .title(title)
                .surveyId(surveyId)
                .questionType(QuestionType.getQuestionType(questionType))
                .imageName(filename)
                .imageUrl(url)
                .build();
        questions.setSequence(sequence);
        questions = questionsRepository.save(questions);
        return questions;
    }

    public Long updateQuestion(QuestionRequestDto questionRequestDto, Long questionId, Long surveyId, String email) {
        surveyFindService.findByIdAndEmail(email, surveyId);
        Questions questions = findById(questionId);
        String title = questionRequestDto.getQuestionDto().getTitle();
        String questionType = questionRequestDto.getQuestionDto().getQuestionType();

        questions.updateQuestion(title, questionType);

        questions = questionsRepository.save(questions);

        return questions.getId();
    }

    public List<Questions> findBySurveyId(Long surveyId) {
        return questionsRepository.findListBySurveyId(surveyId);
    }

    public Page<Questions> findPageBySurveyId(Long surveyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return questionsRepository.findPageBySurveyId(pageable, surveyId);
    }

    //조회는 options와 같이 보여줘야 할 것 같아서 패스

    public Questions findById(Long questionId) {
        return questionsRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 question"));
    }


    public void deleteById(Long questionId) {
        Questions question = findById(questionId);
        questionsRepository.deleteById(questionId);
    }

    public void deleteQuestionByQuestionIdList(List<Long> questionIdList) {
        questionsRepository.deleteAllById(questionIdList);
    }

    public int countQuestionsBySurveyId(Long surveyId) {
        return questionsRepository.countQuestionsBySurveyId(surveyId);
    }
}
