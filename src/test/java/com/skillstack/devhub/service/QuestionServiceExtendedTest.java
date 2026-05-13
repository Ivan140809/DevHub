package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.AnswerResponseDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.exception.QuestionNotFoundException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.*;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;
import com.skillstack.devhub.repository.ReviewRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class QuestionServiceExtendedTest {

    @Mock private QuestionRepository questionRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private AnswerRepository answerRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private QuestionService questionService;

    // ======================== getQuestions - filtros ========================

    @Test
    @CasoPrueba(
            id = "CP400",
            descripcion = "getQuestions - sin filtros retorna todas las preguntas paginadas",
            entrada = "category=null, difficulty=null, page=0",
            tipo = "Normal",
            esperado = "Se retorna lista de QuestionDTO usando findAll con paginación"
    )
    void getQuestions_noFilters_returnsAll() {
        Question q = new Question("Titulo", "Enunciado", Category.BACKEND, Difficulty.EASY, List.of());
        q.setId("q1");
        when(questionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(q)));

        List<QuestionDTO> result = questionService.getQuestions(null, null, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Titulo", result.get(0).getTitle());
        verify(questionRepository).findAll(any(Pageable.class));
    }

    @Test
    @CasoPrueba(
            id = "CP401",
            descripcion = "getQuestions - solo category retorna preguntas filtradas por categoría",
            entrada = "category=BACKEND, difficulty=null, page=0",
            tipo = "Normal",
            esperado = "Se llama findByCategory y retorna lista filtrada"
    )
    void getQuestions_onlyCategory_returnsByCategory() {
        Question q = new Question("Titulo", "Enunciado", Category.BACKEND, Difficulty.EASY, List.of());
        q.setId("q1");
        when(questionRepository.findByCategory(eq(Category.BACKEND), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(q)));

        List<QuestionDTO> result = questionService.getQuestions(Category.BACKEND, null, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Category.BACKEND, result.get(0).getCategory());
        verify(questionRepository).findByCategory(eq(Category.BACKEND), any(Pageable.class));
    }

    @Test
    @CasoPrueba(
            id = "CP402",
            descripcion = "getQuestions - solo difficulty retorna preguntas filtradas por dificultad",
            entrada = "category=null, difficulty=HARD, page=0",
            tipo = "Normal",
            esperado = "Se llama findByDifficulty y retorna lista filtrada"
    )
    void getQuestions_onlyDifficulty_returnsByDifficulty() {
        Question q = new Question("Titulo", "Enunciado", Category.BACKEND, Difficulty.HARD, List.of());
        q.setId("q1");
        when(questionRepository.findByDifficulty(eq(Difficulty.HARD), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(q)));

        List<QuestionDTO> result = questionService.getQuestions(null, Difficulty.HARD, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Difficulty.HARD, result.get(0).getDifficulty());
        verify(questionRepository).findByDifficulty(eq(Difficulty.HARD), any(Pageable.class));
    }

    @Test
    @CasoPrueba(
            id = "CP403",
            descripcion = "getQuestions - ambos filtros retorna preguntas con categoría y dificultad",
            entrada = "category=FRONTEND, difficulty=MEDIUM, page=0",
            tipo = "Normal",
            esperado = "Se llama findByCategoryAndDifficulty y retorna lista filtrada"
    )
    void getQuestions_bothFilters_returnsByCategoryAndDifficulty() {
        Question q = new Question("Titulo", "Enunciado", Category.FRONTEND, Difficulty.MEDIUM, List.of());
        q.setId("q1");
        when(questionRepository.findByCategoryAndDifficulty(
                eq(Category.FRONTEND), eq(Difficulty.MEDIUM), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(q)));

        List<QuestionDTO> result = questionService.getQuestions(Category.FRONTEND, Difficulty.MEDIUM, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(questionRepository).findByCategoryAndDifficulty(
                eq(Category.FRONTEND), eq(Difficulty.MEDIUM), any(Pageable.class));
    }

    @Test
    @CasoPrueba(
            id = "CP404",
            descripcion = "getQuestions - página sin resultados retorna lista vacía",
            entrada = "category=null, difficulty=null, page=99",
            tipo = "Borde",
            esperado = "Se retorna lista vacía sin lanzar excepciones"
    )
    void getQuestions_emptyPage_returnsEmptyList() {
        when(questionRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        List<QuestionDTO> result = questionService.getQuestions(null, null, 99);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ======================== verifyAnswer - casos adicionales ========================

    @Test
    @CasoPrueba(
            id = "CP405",
            descripcion = "verifyAnswer - usuario no encontrado lanza UserNotFoundException",
            entrada = "questionId válido, userEmail=noexiste@test.com",
            tipo = "Negativa",
            esperado = "UserNotFoundException lanzada"
    )
    void verifyAnswer_userNotFound_throwsUserNotFoundException() {
        Question question = new Question();
        question.setId("q1");
        question.setOptions(List.of(new Option("A", true)));
        when(questionRepository.findById("q1")).thenReturn(Optional.of(question));
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setSelectedOption("A");
        answerDTO.setTimerDTO(Duration.of(10, ChronoUnit.SECONDS));

        assertThrows(UserNotFoundException.class,
                () -> questionService.verifyAnswer(answerDTO, "q1", "noexiste@test.com"));
    }

    @Test
    @CasoPrueba(
            id = "CP406",
            descripcion = "verifyAnswer - pregunta no encontrada lanza QuestionNotFoundException",
            entrada = "questionId=noexiste, userEmail válido",
            tipo = "Negativa",
            esperado = "QuestionNotFoundException lanzada"
    )
    void verifyAnswer_questionNotFound_throwsQuestionNotFoundException() {
        when(questionRepository.findById("noexiste")).thenReturn(Optional.empty());

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setSelectedOption("A");
        answerDTO.setTimerDTO(Duration.of(10, ChronoUnit.SECONDS));

        assertThrows(QuestionNotFoundException.class,
                () -> questionService.verifyAnswer(answerDTO, "noexiste", "user@test.com"));
    }

    @Test
    @CasoPrueba(
            id = "CP407",
            descripcion = "verifyAnswer - respuesta correcta pero pregunta ya respondida no incrementa score",
            entrada = "questionId ya en answeredQuestions del usuario",
            tipo = "Logica Negocio",
            esperado = "isCorrect=true, totalScore sin cambios porque ya respondió antes"
    )
    void verifyAnswer_correctButAlreadyAnswered_scoreUnchanged() {
        String questionId = "q-already";
        Option correctOption = new Option("Correcta", true);
        Question question = new Question();
        question.setId(questionId);
        question.setDifficulty(Difficulty.EASY);
        question.setOptions(List.of(correctOption));

        User user = new User();
        user.setId("u1");
        user.setEmail("user@test.com");
        user.setTotalScore(10);

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setSelectedOption("Correcta");
        answerDTO.setTimerDTO(Duration.of(5, ChronoUnit.SECONDS));

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(answerRepository.findDistinctQuestionIdByUserId("u1"))
                .thenReturn(List.of(questionId));

        AnswerResponseDTO response = questionService.verifyAnswer(answerDTO, questionId, "user@test.com");

        assertTrue(response.isCorrect());
        assertEquals(10, user.getTotalScore(), "Score no debe cambiar si ya respondió la pregunta");
        verify(userRepository, never()).save(any());
    }

    @Test
    @CasoPrueba(
            id = "CP408",
            descripcion = "verifyAnswer - dificultad HARD otorga más puntos que EASY",
            entrada = "questionId HARD, respuesta correcta, primera vez",
            tipo = "Normal",
            esperado = "El totalScore aumenta con los puntos de HARD"
    )
    void verifyAnswer_hardDifficulty_grantsMorePoints() {
        Option correctOption = new Option("Correcta", true);
        Question question = new Question();
        question.setId("q-hard");
        question.setDifficulty(Difficulty.HARD);
        question.setOptions(List.of(correctOption));

        User user = new User();
        user.setId("u1");
        user.setEmail("user@test.com");
        user.setTotalScore(0);

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setSelectedOption("Correcta");
        answerDTO.setTimerDTO(Duration.of(5, ChronoUnit.SECONDS));

        when(questionRepository.findById("q-hard")).thenReturn(Optional.of(question));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(answerRepository.findDistinctQuestionIdByUserId("u1")).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        AnswerResponseDTO response = questionService.verifyAnswer(answerDTO, "q-hard", "user@test.com");

        assertTrue(response.isCorrect());
        assertTrue(user.getTotalScore() > 1, "HARD debe otorgar más de 1 punto");
        verify(userRepository).save(user);
    }

    // ======================== getReviewsByQuestionId - casos adicionales ========================

    @Test
    @CasoPrueba(
            id = "CP409",
            descripcion = "getReviewsByQuestionId - pregunta no encontrada lanza QuestionNotFoundException",
            entrada = "questionId=noexiste, page=0",
            tipo = "Negativa",
            esperado = "QuestionNotFoundException lanzada"
    )
    void getReviewsByQuestionId_questionNotFound_throwsException() {
        when(questionRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class,
                () -> questionService.getReviewsByQuestionId("noexiste", 0));
    }

    // ======================== createReview - casos adicionales ========================

    @Test
    @CasoPrueba(
            id = "CP410",
            descripcion = "createReview - usuario no encontrado lanza UserNotFoundException",
            entrada = "email=noexiste@test.com, questionId válido",
            tipo = "Negativa",
            esperado = "UserNotFoundException lanzada"
    )
    void createReview_userNotFound_throwsUserNotFoundException() {
        when(userRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                questionService.createReview(
                        new com.skillstack.devhub.dto.ReviewDTO("Comentario", 3),
                        "noexiste@test.com",
                        "q1"
                )
        );
    }
}
