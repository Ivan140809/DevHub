package com.skillstack.devhub.builder;

import com.skillstack.devhub.model.Progress;
import com.skillstack.devhub.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressDirector {
    private final StatisticsService statisticsService;
    private ProgressBuilder progressBuilder;

    @Autowired
    public ProgressDirector(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    public Progress buildTotalAndPercentage (String userId){
        progressBuilder = new ProgressBuilder(statisticsService);
        progressBuilder.forUser(userId);
        progressBuilder.buildTotal();
        progressBuilder.buildPercentage();
        return progressBuilder.getResult();
    }
}
