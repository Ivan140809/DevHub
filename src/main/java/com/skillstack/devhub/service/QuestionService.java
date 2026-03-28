package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.OptionDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.dto.ReviewDTO;
import com.skillstack.devhub.exception.QuestionAlreadyExistsException;
import com.skillstack.devhub.exception.QuestionNotFoundException;
import com.skillstack.devhub.exception.ReviewNotFoundException;
import com.skillstack.devhub.model.*;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;
import com.skillstack.devhub.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ReviewRepository reviewRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ReviewRepository reviewRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.reviewRepository = reviewRepository;
        this.answerRepository=answerRepository;
    }

    public String addQuestion (QuestionDTO question){
        if (questionRepository.findByTitle(question.getTitulo()).isPresent()){
            throw new QuestionAlreadyExistsException("Pregunta con el titulo "+question.getTitulo()+" ya existe");
        }

        List <Option> options = new ArrayList<>();
        for (OptionDTO optionDTO: question.getOpciones()){
            options.add(new Option(optionDTO.getTexto(), optionDTO.getEsCorrecta()));
        }

        Question q = new Question(question.getTitulo(), question.getEnunciado(), question.getCategoria(), question.getDificultad(), options);

        questionRepository.save(q);

        return "PREGUNTA CREADA EXITOSAMENTE";
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

    public List<QuestionDTO> getQuestionByCategory(Category category, int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findByCategory(category, pageable);

        if(questionPage.isEmpty()){
            throw new QuestionNotFoundException("PREGUNTAS NO ENCONTRADAS CON CATEGORIA "+category);
        }

        return questionPage.getContent().stream().map(q ->
                new QuestionDTO(
                    q.getTitle(),
                    null,
                    q.getCategoria(),
                    q.getDificultad(),
                    null
            )).toList();
    }

    public QuestionDTO getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(()-> new QuestionNotFoundException("PREGUNTA CON ID "+id+" NO ENCONTRADA"));

        List<OptionDTO> options = question.getOpciones().stream().map(r ->  new OptionDTO(r.getTexto(), r.getEsCorrecta())).toList();

        return new QuestionDTO(
                question.getTitle(),
                question.getEnunciado(),
                question.getCategoria(),
                question.getDificultad(),
                options
        );

    }


    public List<QuestionDTO> getQuestionByDifficulty (Difficulty difficulty, int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findByDifficulty(difficulty, pageable);

        if(questionPage.isEmpty()){
            throw new QuestionNotFoundException("PREGUNTAS NO ENCONTRADAS CON DIFICULTAD "+difficulty);
        }

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

    public boolean verifyAnswer (AnswerDTO answerDTO, String id){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID "+id+" NO ENCONTRADA"));

        Answer answer = new Answer(answerDTO.getQuestionId(), answerDTO.getSelectedOption());
        answerRepository.save(answer);

        for (Option option : question.getOpciones()){
            if (option.getTexto().equals(answerDTO.getSelectedOption())){
                return option.getEsCorrecta();
            }
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Respuesta no válida"
        );
    }

    public String createReview(ReviewDTO reviewDTO, String userID, String questionId){

        Review review = new Review(reviewDTO.getComment(), reviewDTO.getRating(),
                questionId, userID, LocalDate.now());

        reviewRepository.save(review);
        
        return "REVIEW CREADO CORRECTAMENTE PARA USUARIO "+userID+" EN LA PREGUNTA "+questionId;
    }

    public List<ReviewDTO> getReviewsByQuestionId(String questionId, int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Review> reviewPage = reviewRepository.findByQuestionId(questionId, pageable);

        if(reviewPage.isEmpty()){
            throw new ReviewNotFoundException("REVIEWS PARA LA PREGUNTA "+questionId+" NO ENCONTRADOS");

        }

        return reviewPage.getContent().stream().map(r ->
            new ReviewDTO(
                r.getComment(),
                r.getRating())
        ).toList();
    }
}
