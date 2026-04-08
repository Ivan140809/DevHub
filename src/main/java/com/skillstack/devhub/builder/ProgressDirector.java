package com.skillstack.devhub.builder;

import com.skillstack.devhub.model.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ProgressDirector {
    private final ApplicationContext context;

    @Autowired
    public ProgressDirector(ApplicationContext context) {
        this.context = context;
    }

    public Progress buildTotalAndPercentage (String userId){
        ProgressBuilder progressBuilder = context.getBean(ProgressBuilder.class);
        progressBuilder.forUser(userId);
        progressBuilder.buildTotal();
        progressBuilder.buildPercentage();
        return progressBuilder.getResult();
    }
}
