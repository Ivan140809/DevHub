package com.skillstack.devhub.controller;


import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
