package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import com.skillstack.devhub.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@CrossOrigin
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createQuestion(@RequestBody QuestionDTO question) {
        questionService.addQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pregunta Creada Exitosamente");
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@RequestParam(defaultValue = "0") int page) {
        List<QuestionDTO> questions = questionService.getQuestions(page);

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionDTO>> filterCategory(@PathVariable Category category, @RequestParam(defaultValue = "0") int page) {
        List<QuestionDTO> questions = questionService.getQuestionByCategory(category, page);

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<QuestionDTO>> filterDifficulty(@PathVariable Difficulty difficulty, @RequestParam(defaultValue = "0") int page){
        List<QuestionDTO> questions = questionService.getQuestionByDifficulty(difficulty, page);

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable String id) {
        QuestionDTO question = questionService.getQuestionDTOById(id);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/categories")
    public ResponseEntity<Category[]> getCategories() {
        return ResponseEntity.ok(Category.values());
    }

    @GetMapping("/{id}/answer")
    public ResponseEntity<Boolean> answer(@PathVariable String id, @Valid @RequestBody AnswerDTO answer){

        boolean response = questionService.verifyAnswer(answer);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
