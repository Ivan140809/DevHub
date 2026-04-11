package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.dto.ReviewDTO;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import com.skillstack.devhub.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/questions")
@CrossOrigin
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createQuestion(@RequestBody QuestionDTO question) {
        String response = questionService.addQuestion(question);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@RequestParam(defaultValue = "0") int page) {
        List<QuestionDTO> questions = questionService.getQuestions(page);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionDTO>> filterCategory(@PathVariable Category category, @RequestParam(defaultValue = "0") int page) {
        List<QuestionDTO> questions = questionService.getQuestionByCategory(category, page);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questions);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<QuestionDTO>> filterDifficulty(@PathVariable Difficulty difficulty, @RequestParam(defaultValue = "0") int page){
        List<QuestionDTO> questions = questionService.getQuestionByDifficulty(difficulty, page);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(questions);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable String id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(question);
    }



    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/answer")
    public ResponseEntity<Boolean> answer(@PathVariable String id, @Valid @RequestBody AnswerDTO answer,
                                          Authentication authentication){

        boolean response = questionService.verifyAnswer(answer,id, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/reviews")
    public ResponseEntity<String> createReview(
        @PathVariable("id") String questionId,
        @RequestBody ReviewDTO reviewDTO,
        Authentication authentication){

        String response = questionService.createReview(reviewDTO, authentication.getName(), questionId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByQuestionId(@PathVariable String id, @RequestParam(defaultValue = "0") int page){
        List<ReviewDTO> reviews = questionService.getReviewsByQuestionId(id, page);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/categories")
    public ResponseEntity<Category[]> getCategories() {
        return ResponseEntity.ok(Category.values());
    }
}
