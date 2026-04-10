package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.OptionDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.dto.ReviewDTO;
import com.skillstack.devhub.exception.QuestionAlreadyExistsException;
import com.skillstack.devhub.exception.QuestionNotFoundException;
import com.skillstack.devhub.exception.ReviewNotFoundException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.*;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;
import com.skillstack.devhub.repository.ReviewRepository;
import com.skillstack.devhub.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ReviewRepository reviewRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.reviewRepository = reviewRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public String addQuestion(QuestionDTO question) {
        if (questionRepository.findByTitle(question.getTitle()).isPresent()) {
            throw new QuestionAlreadyExistsException("Pregunta con el titulo " + question.getTitle() + " ya existe");
        }

        List<Option> options = new ArrayList<>();
        for (OptionDTO optionDTO : question.getOptions()) {
            options.add(new Option(optionDTO.getText(), optionDTO.isCorrect()));
        }

        Question q = new Question(question.getTitle(), question.getStatement(), question.getCategory(), question.getDifficulty(), options);

        questionRepository.save(q);

        return "PREGUNTA CREADA EXITOSAMENTE";
    }

    public List<QuestionDTO> getQuestions(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findAll(pageable);

        return questionPage.getContent().stream().map(q ->
                new QuestionDTO(
                        q.getId(),
                        q.getTitle(),
                        null,
                        q.getCategory(),
                        q.getDifficulty(),
                        null
                )
        ).toList();
    }

    public List<QuestionDTO> getQuestionByCategory(Category category, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findByCategory(category, pageable);

        if (questionPage.isEmpty()) {
            throw new QuestionNotFoundException("PREGUNTAS NO ENCONTRADAS CON CATEGORIA " + category);
        }

        return questionPage.getContent().stream().map(q ->
                new QuestionDTO(
                        q.getId(),
                        q.getTitle(),
                        null,
                        q.getCategory(),
                        q.getDifficulty(),
                        null
                )).toList();
    }

    public QuestionDTO getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID " + id + " NO ENCONTRADA"));

        List<OptionDTO> options = question.getOptions().stream()
                .map(r -> new OptionDTO(r.getText(), r.isCorrect())).toList();

        return new QuestionDTO(
                question.getId(),
                question.getTitle(),
                question.getStatement(),
                question.getCategory(),
                question.getDifficulty(),
                options
        );
    }

    public List<QuestionDTO> getQuestionByDifficulty(Difficulty difficulty, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findByDifficulty(difficulty, pageable);

        if (questionPage.isEmpty()) {
            throw new QuestionNotFoundException("PREGUNTAS NO ENCONTRADAS CON DIFICULTAD " + difficulty);
        }

        return questionPage.getContent().stream().map(q ->
                new QuestionDTO(q.getId(),
                        q.getTitle(),
                        null,
                        q.getCategory(),
                        q.getDifficulty(),
                        null
                )
        ).toList();
    }

    public boolean verifyAnswer(AnswerDTO answerDTO, String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID " + id + " NO ENCONTRADA"));

        Answer answer = new Answer(answerDTO.getQuestionId(), answerDTO.getSelectedOption(), answerDTO.getUserId());
        answerRepository.save(answer);

        for (Option option : question.getOptions()) {
            if (option.getText().equals(answerDTO.getSelectedOption())) {
                return option.isCorrect();
            }
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Respuesta invalida"
        );
    }

    public String createReview(ReviewDTO reviewDTO, String id, String questionId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("USUARIO NO ENCONTRADO"));

        Review review = new Review(reviewDTO.getComment(), reviewDTO.getRating(), questionId, user.getId(), LocalDate.now());

        reviewRepository.save(review);

        return "REVIEW CREADO CORRECTAMENTE PARA USUARIO " + user.getId() + " EN LA PREGUNTA " + questionId;
    }

    public List<ReviewDTO> getReviewsByQuestionId(String questionId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Review> reviewPage = reviewRepository.findByQuestionId(questionId, pageable);

        if (reviewPage.isEmpty()) {
            throw new ReviewNotFoundException("REVIEWS PARA LA PREGUNTA " + questionId + " NO ENCONTRADOS");
        }

        return reviewPage.getContent().stream().map(r ->
                new ReviewDTO(
                        r.getComment(),
                        r.getRating())
        ).toList();
    }
}
