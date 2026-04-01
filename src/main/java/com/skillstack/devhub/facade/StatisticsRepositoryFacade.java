package com.skillstack.devhub.facade;

import com.skillstack.devhub.exception.QuestionNotFoundException;
import com.skillstack.devhub.model.Answer;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;

import java.util.List;

public class StatisticsRepositoryFacade {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public StatisticsRepositoryFacade(AnswerRepository answerRepository,
                                      QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    public List<Answer>  findAnseredQuestionsByUser(String userId){
        return answerRepository.findByUserId(userId);
    }

    public Question findQuestionById(String questionId){
        return questionRepository.findById(questionId)
                .orElseThrow(()-> new QuestionNotFoundException("PREGUNTA CON ID"+questionId+"NO ENCONTRADA"));
    }

}
