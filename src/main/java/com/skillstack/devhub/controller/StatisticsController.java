package com.skillstack.devhub.controller;

import com.skillstack.devhub.builder.ProgressDirector;
import com.skillstack.devhub.dto.ProgressDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.model.Progress;
import com.skillstack.devhub.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ProgressDirector progressDirector;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, ProgressDirector progressDirector) {
        this.statisticsService = statisticsService;
        this.progressDirector = progressDirector;
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/answered")
    public ResponseEntity<List<QuestionDTO>> getAnsweredQuestions(Authentication authentication) {
        List<QuestionDTO> questions = statisticsService.progress("AnsweredQuestionsStatistic", authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    //@PreAuthorize("hasRole('USER')")
    @GetMapping("/progress")
    public ResponseEntity<ProgressDTO> getProgress(Authentication authentication) {
        Progress progress = progressDirector.buildTotalAndPercentage(authentication.getName());
        ProgressDTO progressDTO = new ProgressDTO(progress.getTotalAnswered(), progress.getPercentage());
        return ResponseEntity.status(HttpStatus.OK).body(progressDTO);
    }
}
