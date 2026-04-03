package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/answered")
    public ResponseEntity<List<QuestionDTO>> getAnsweredQuestions(Principal principal) {
        List<QuestionDTO> questions = statisticsService.execute("AnsweredQuestionsStatistic", principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

}
