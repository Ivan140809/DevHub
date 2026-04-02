package com.skillstack.devhub.strategy;

import com.skillstack.devhub.facade.StatisticsRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TotalQuestionsAnsweredStatistics implements Statistics <Integer> {
    private final StatisticsRepositoryFacade statisticsRepositoryFacade;

    @Autowired
    public TotalQuestionsAnsweredStatistics(StatisticsRepositoryFacade statisticsRepositoryFacade) {
        this.statisticsRepositoryFacade = statisticsRepositoryFacade;
    }

    @Override
    public Integer progress (String userId){
        return statisticsRepositoryFacade.findAnsweredQuestionsByUser(userId).size();
    }
}
