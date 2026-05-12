package com.skillstack.devhub.service;

import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.AnswerResponseDTO;
import com.skillstack.devhub.dto.OptionDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.dto.ReviewDTO;
import com.skillstack.devhub.exception.QuestionAlreadyExistsException;
import com.skillstack.devhub.exception.QuestionNotFoundException;
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

    private static final String NOT_FOUND_SUFFIX = " NO ENCONTRADA";

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
            options.add(optionDTO.toOption());
        }

        Question q = new Question(question.getTitle(), question.getStatement(), question.getCategory(), question.getDifficulty(), options);

        questionRepository.save(q);

        return "PREGUNTA CREADA EXITOSAMENTE";
    }

    public QuestionDTO getQuestionById(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID " + id + NOT_FOUND_SUFFIX));

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

    public List<QuestionDTO> getQuestions(Category category, Difficulty difficulty, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Question> questionPage;

        if (category != null && difficulty != null) {
            questionPage = questionRepository.findByCategoryAndDifficulty(category, difficulty, pageable);
        } else if (category != null) {
            questionPage = questionRepository.findByCategory(category, pageable);
        } else if (difficulty != null) {
            questionPage = questionRepository.findByDifficulty(difficulty, pageable);
        } else {
            questionPage = questionRepository.findAll(pageable);
        }

        return questionPage.getContent().stream()
                .map(question -> new QuestionDTO(
                        question.getId(),
                        question.getTitle(),
                        question.getStatement(),
                        question.getCategory(),
                        question.getDifficulty(),
                        question.getOptions() == null ? List.of() :
                                question.getOptions().stream()
                                        .map(option -> new OptionDTO(option.getText(), option.isCorrect()))
                                        .toList()
                ))
                .toList();
    }

    public AnswerResponseDTO verifyAnswer(AnswerDTO answerDTO, String questionId, String userEmail) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID " + questionId + NOT_FOUND_SUFFIX));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("USUARIO CON EMAIL " + userEmail + " NO ENCONTRADO"));

        Answer answer = new Answer(questionId, answerDTO.getSelectedOption(), user.getId(), answerDTO.getTimerDTO());
        answerRepository.save(answer);

        for (Option option : question.getOptions()) {
            if (option.getText().equals(answerDTO.getSelectedOption())) {
                boolean isCorrect = option.isCorrect();
                if (isCorrect) {
                    user.setTotalScore(user.getTotalScore() + question.getDifficulty().getPoints());
                    userRepository.save(user);
                }
                return new AnswerResponseDTO(isCorrect, user.getTotalScore());
            }
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Respuesta invalida"
        );
    }

    public String createReview(ReviewDTO reviewDTO, String id, String questionId) {
        User user = userRepository.findByEmail(id)
                .orElseThrow(() -> new UserNotFoundException("USUARIO NO ENCONTRADO"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID " + questionId + NOT_FOUND_SUFFIX));
        Review review = new Review(reviewDTO.getComment(), reviewDTO.getRating(), question.getId(), user.getId(), LocalDate.now());

        reviewRepository.save(review);

        return "REVIEW CREADO CORRECTAMENTE PARA USUARIO " + user.getId() + " EN LA PREGUNTA " + questionId;
    }

    public List<ReviewDTO> getReviewsByQuestionId(String questionId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("PREGUNTA CON ID " + questionId + NOT_FOUND_SUFFIX));
        Page<Review> reviewPage = reviewRepository.findByQuestionId(questionId, pageable);

        return reviewPage.getContent().stream().map(r ->
                new ReviewDTO(
                        r.getComment(),
                        r.getRating())
        ).toList();
    }
}
