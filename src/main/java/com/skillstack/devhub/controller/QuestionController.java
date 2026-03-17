package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.service.QuestionService;
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
    public ResponseEntity<String> createQuestion(QuestionDTO question) {
        questionService.addQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pregunta Creada Exitosamente");
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@RequestParam(defaultValue = "0") int page) {
        List<QuestionDTO> questions = questionService.getQuestions(page);

        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(questions);
        }

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionDTO>> filterCategory(@PathVariable Category category, @RequestParam(defaultValue = "0") int page) {
        List<QuestionDTO> questions = questionService.getQuestionByCategory(category, page);

        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(questions);
        }

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/dificultad/{dificultad}")
    public ResponseEntity<List<QuestionDTO>> filterDifficulty(@PathVariable Dificultad dificultad, @RequestParam(defaultValue = "0") int page){
        List<QuestionDTO> questions = questionService.getQuestionByDificultad(dificultad, page);

        if (questions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(questions);
        }

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/categories")
    public ResponseEntity<Category[]> getCategories() {
        return ResponseEntity.ok(Category.values());
    }
}
