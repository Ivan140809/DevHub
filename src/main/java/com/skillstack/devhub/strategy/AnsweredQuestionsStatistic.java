package com.skillstack.devhub.strategy;

import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.facade.StatisticsRepositoryFacade;
import com.skillstack.devhub.model.Answer;
import com.skillstack.devhub.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnsweredQuestionsStatistic implements Statistics<List<QuestionDTO>>{

    StatisticsRepositoryFacade statisticsRepositoryFacade;

    @Autowired
    public AnsweredQuestionsStatistic(StatisticsRepositoryFacade statisticsRepositoryFacade) {
        this.statisticsRepositoryFacade = statisticsRepositoryFacade;
    }


    @Override
    public List<QuestionDTO> progress(String userId){
        List<String> questionsId = statisticsRepositoryFacade.findAnsweredQuestionsByUser(userId);
        List<Question> questions = new ArrayList<>();
        for(String questionId: questionsId){
            Question q = statisticsRepositoryFacade.findQuestionById(questionId);
            questions.add(q);
        }

        return questions.stream().map(q ->
                new QuestionDTO(
                        q.getTitle(),
                        null,
                        q.getCategoria(),
                        q.getDificultad(),
                        null)).toList();
    }




}
