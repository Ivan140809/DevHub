package com.skillstack.devhub.controller;

import com.skillstack.devhub.builder.ProgressBuilder;
import com.skillstack.devhub.builder.ProgressDirector;
import com.skillstack.devhub.dto.ProgressDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.Progress;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProgressDirector progressDirector;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, UserRepository userRepository, ProgressDirector progressDirector) {
        this.statisticsService = statisticsService;
        this.userRepository = userRepository;
        this.progressDirector = progressDirector;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/answered")
    public ResponseEntity<List<QuestionDTO>> getAnsweredQuestions(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("USUARIO NO ENCONTRADO"));

        List<QuestionDTO> questions = statisticsService.progress("AnsweredQuestionsStatistic", user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/progress")
    public ResponseEntity<ProgressDTO> getProgress(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("USUARIO NO ENCONTRADO"));

        Progress progress = progressDirector.buildTotalAndPercentage(user.getId());
        ProgressDTO progressDTO = new ProgressDTO(progress.getTotalAnswered(), progress.getTotalAnswered());
        return ResponseEntity.status(HttpStatus.OK).body(progressDTO);
    }
}
