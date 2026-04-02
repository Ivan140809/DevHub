package com.skillstack.devhub.strategy;

import com.skillstack.devhub.facade.StatisticsRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PercentageQuestionsAnsweredStatistics implements Statistics <Double>{
    private final StatisticsRepositoryFacade statisticsRepositoryFacade;

    @Autowired
    public PercentageQuestionsAnsweredStatistics(StatisticsRepositoryFacade statisticsRepositoryFacade) {
        this.statisticsRepositoryFacade = statisticsRepositoryFacade;
    }

    @Override
    public Double progress(String userId) {
        return statisticsRepositoryFacade.percentageAnsweredQuestions(userId);
    }
}
