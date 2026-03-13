package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.OptionDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Option;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void addQuestion (QuestionDTO question){
        if (questionRepository.findByTitle(question.getTitulo()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una pregunta con este titulo"
            );
        }

        List <Option> options = new ArrayList<>();
        for (OptionDTO optionDTO: question.getOpciones()){
            options.add(new Option(optionDTO.getTexto(), optionDTO.getEsCorrecta()));
        }

        Question q = new Question(question.getTitulo(), question.getEnunciado(), question.getCategoria(), question.getDificultad(), options);

        questionRepository.save(q);
    }

    public List<QuestionDTO> getQuestionByCategory(Category category){

        List<Question> questions = questionRepository.findByCategory(category);

        return questions.stream().map(q -> new QuestionDTO(
                q.getTitle(),
                null,
                q.getCategoria(),
                q.getDificultad(),
                null
        )).toList();
    }
}
