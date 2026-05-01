package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.AnswerResponseDTO;
import com.skillstack.devhub.dto.ReviewDTO;
import com.skillstack.devhub.model.Difficulty;
import com.skillstack.devhub.model.Option;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.model.Review;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class QuestionTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionService questionService;

    // CP14 - Normal: Verificar pregunta con respuesta correcta
    @Test
    @CasoPrueba(
        id          = "CP14",
        descripcion = "Verificar pregunta con respuesta correcta",
        entrada     = "questionId=69e005ee4d818c49ab701225, userEmail=verify@test.com, selectedOption=Contenedor",
        tipo        = "Normal",
        esperado    = "isCorrect=true, totalScore incrementado en 1 punto (dificultad EASY)"
    )
    void verifyAnswer_whenCorrectAnswer_returnsIsCorrectTrueAndIncreasesScore() {

        String questionId = "69e005ee4d818c49ab701225";
        String userEmail  = "verify@test.com";

        Option optionCorrecta   = new Option("Contenedor", true);
        Option optionIncorrecta = new Option("Imagen", false);

        Question questionFake = new Question();
        questionFake.setId(questionId);
        questionFake.setDifficulty(Difficulty.EASY);
        questionFake.setOptions(List.of(optionCorrecta, optionIncorrecta));

        User userFake = new User();
        userFake.setEmail(userEmail);
        userFake.setTotalScore(0);

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setSelectedOption("Contenedor");

        when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(questionFake));
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(userFake));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AnswerResponseDTO response = questionService.verifyAnswer(answerDTO, questionId, userEmail);

        assertTrue(response.isCorrect(),
                "La respuesta es marcada como correcta");
        assertEquals(1, userFake.getTotalScore(),
                "El totalScore se incrementa 1 punto");

        verify(userRepository).save(userFake);

        System.out.println("CP14 — isCorrect: " + response.isCorrect());
        System.out.println("CP14 — totalScore tras respuesta: " + userFake.getTotalScore());
    }

    // CP15 - Normal: Obtener lista de reviews de una pregunta
    @Test
    @CasoPrueba(
        id          = "CP15",
        descripcion = "Obtener lista de reviews de una pregunta",
        entrada     = "questionId=69e005ee4d818c49ab701225, page=0",
        tipo        = "Normal",
        esperado    = "Se retorna una lista de ReviewDTO con comment y rating de cada review"
    )
    void getReviewsByQuestionId_whenValidId_returnsReviewDTOList() {

        String questionId = "69e005ee4d818c49ab701225";
        int page = 0;

        Question questionFake = new Question();
        questionFake.setId(questionId);
        when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(questionFake));

        Review review1 = new Review();
        review1.setComment("Muy fácil");
        review1.setRating(3);

        Review review2 = new Review();
        review2.setComment("Bastante clara");
        review2.setRating(4);

        when(reviewRepository.findByQuestionId(eq(questionId), any()))
                .thenReturn(new PageImpl<>(List.of(review1, review2)));

        List<ReviewDTO> result = questionService.getReviewsByQuestionId(questionId, page);

        assertNotNull(result, "El resultado no debería ser nulo");
        assertFalse(result.isEmpty(), "La lista de reviews no debería estar vacía");
        assertEquals("Muy fácil",      result.get(0).getComment());
        assertEquals(3,                result.get(0).getRating());
        assertEquals("Bastante clara", result.get(1).getComment());
        assertEquals(4,                result.get(1).getRating());

        verify(reviewRepository, times(1)).findByQuestionId(eq(questionId), any());

        System.out.println("CP15 — Reviews retornados: " + result.size());
        result.forEach(r ->
            System.out.println("  comment: " + r.getComment() + ". Rating: " + r.getRating())
        );
    }

    // CP19 - Borde: Crear review utilizando rating mínimo
    @Test
    @CasoPrueba(
        id          = "CP19",
        descripcion = "Crear review utilizando rating mínimo",
        entrada     = "questionId=69e005ee4d818c49ab701225, userEmail=verify@test.com, reviewDTO={Imposible, 1}",
        tipo        = "Borde",
        esperado    = "Review creado correctamente con rating mínimo de 1"
    )
    void createReview_whenMinimumRating_createsReviewSuccessfully() {

        String questionId = "69e005ee4d818c49ab701225";

        User userFake = new User();
        userFake.setUsername("pepe");
        userFake.setEmail("verify@test.com");
        userFake.setRole(Role.USER);

        Question questionFake = new Question();
        questionFake.setId(questionId);

        ReviewDTO reviewDTO = new ReviewDTO("Imposible", 1);

        when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(questionFake));
        when(userRepository.findByEmail(userFake.getEmail()))
                .thenReturn(Optional.of(userFake));
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // El servicio retorna: "REVIEW CREADO CORRECTAMENTE PARA USUARIO " + user.getId() + " EN LA PREGUNTA " + questionId
        // Como userFake se crea con new User() y no se persiste, getId() retorna null
        String expectedMessage = "REVIEW CREADO CORRECTAMENTE PARA USUARIO "
                + userFake.getId()
                + " EN LA PREGUNTA " + questionId;

        String result = questionService.createReview(questionId, userFake.getEmail(), reviewDTO);

        assertNotNull(result, "El mensaje de resultado no debería ser nulo");
        assertEquals(expectedMessage, result,
                "El mensaje de confirmación no coincide con el esperado");

        verify(reviewRepository).save(any(Review.class));

        System.out.println("CP19 — " + result);
    }

    // CP20 - Lógica de Negocio: Verificar pregunta con respuesta incorrecta
    @Test
    @CasoPrueba(
        id          = "CP20",
        descripcion = "Verificar pregunta con respuesta incorrecta",
        entrada     = "questionId=69e005ee4d818c49ab701225, userEmail=verify@test.com, selectedOption=Imagen",
        tipo        = "Logica Negocio",
        esperado    = "isCorrect=false, totalScore sin cambios"
    )
    void verifyAnswer_whenIncorrectAnswer_returnsIsCorrectFalseAndScoreUnchanged() {

        String questionId   = "69e005ee4d818c49ab701225";
        String userEmail    = "verify@test.com";
        int    scoreInicial = 5;

        Option optionCorrecta   = new Option("Contenedor", true);
        Option optionIncorrecta = new Option("Imagen", false);

        Question questionFake = new Question();
        questionFake.setId(questionId);
        questionFake.setDifficulty(Difficulty.EASY);
        questionFake.setOptions(List.of(optionCorrecta, optionIncorrecta));

        User userFake = new User();
        userFake.setEmail(userEmail);
        userFake.setTotalScore(scoreInicial);

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setSelectedOption("Imagen");

        when(questionRepository.findById(questionId))
                .thenReturn(Optional.of(questionFake));
        when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(userFake));

        AnswerResponseDTO response = questionService.verifyAnswer(answerDTO, questionId, userEmail);

        assertFalse(response.isCorrect(),
                "La respuesta incorrecta debe retornar isCorrect = false");
        assertEquals(scoreInicial, userFake.getTotalScore(),
                "El totalScore no debio modificarse");

        verify(userRepository, never()).save(any(User.class));

        System.out.println("CP20 — isCorrect: " + response.isCorrect());
        System.out.println("CP20 — totalScore: " + userFake.getTotalScore());
    }
}
