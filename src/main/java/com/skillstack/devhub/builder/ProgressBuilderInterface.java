package com.skillstack.devhub.builder;

import com.skillstack.devhub.model.Progress;

public interface ProgressBuilderInterface {
    void buildTotal();
    void buildPercentage();
    Progress getResult();
}
