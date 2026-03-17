package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.OptionDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Option;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    public List<QuestionDTO> getQuestions(int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findAll(pageable);

        return questionPage.getContent().stream().map(q ->
                new QuestionDTO(
                        q.getTitle(),
                        null,
                        q.getCategoria(),
                        q.getDificultad(),
                        null
                )
        ).toList();
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


    public QuestionDTO getQuestionByTitle(String title){
        Question question = questionRepository.findByTitle(title)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pregunta no encontrada")
        );

        List<OptionDTO> options = question.getOpciones().stream().map(o-> new OptionDTO(
                o.getTexto(),
                o.getEsCorrecta()
        )).toList();

        return new QuestionDTO(question.getTitle(), question.getEnunciado(), question.getCategoria(),
                question.getDificultad(), options);
    }
}
