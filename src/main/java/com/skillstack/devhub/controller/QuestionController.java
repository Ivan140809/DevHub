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
    QuestionController(QuestionService questionService){this.questionService = questionService;}

   @PostMapping("/add")
    public ResponseEntity<String> createQuestion(QuestionDTO question){
        questionService.addQuestion(question);
       return ResponseEntity.status(HttpStatus.CREATED).body("Pregunta Creada Exitosamente");
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionDTO>> filterQuestion(@PathVariable Category category) {
        //se debe implementar este metodo
        List<QuestionDTO> questions = questionService.getQuestionByCategory(category);

        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(questions);
        }

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/categories")
    public ResponseEntity<Category[]> getCategories(){
        return ResponseEntity.ok(Category.values());
    }

}
