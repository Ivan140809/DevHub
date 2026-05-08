package com.skillstack.devhub.facade;

import com.skillstack.devhub.exception.QuestionNotFoundException;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsRepositoryFacade {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public StatisticsRepositoryFacade(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public List<String> findAnsweredQuestionsByUser(String userId){
        return answerRepository.findDistinctQuestionIdByUserId(userId);
    }

    public Question findQuestionById(String questionId){
        return questionRepository.findById(questionId)
                .orElseThrow(()-> new QuestionNotFoundException("PREGUNTA CON ID "+questionId+" NO ENCONTRADA"));
    }

    public long countTotalQuestions(){
        return questionRepository.count();
    }
}
