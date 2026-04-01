package com.skillstack.devhub.service;

import com.skillstack.devhub.strategy.Statistics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final Map<String, Statistics<?>> strategies;

    public StatisticsService(List<Statistics<?>> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getClass().getSimpleName(),
                        s -> s
                ));
    }

    @SuppressWarnings("unchecked")
    public <T> T execute(String strategyName, String userId) {
        Statistics<T> strategy = (Statistics<T>) strategies.get(strategyName);
        return strategy.progress(userId);
    }


}
