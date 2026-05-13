package com.skillstack.devhub.controller;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.AnswerDTO;
import com.skillstack.devhub.dto.AnswerResponseDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.dto.ReviewDTO;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import com.skillstack.devhub.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;


    // CP09 - Normal: Crear pregunta con datos validos
    @Test
    @CasoPrueba(
            id = "CP09",
            descripcion = "Crear pregunta con datos validos",
            entrada = "QuestionDTO con datos validos",
            tipo = "Normal",
            esperado = "Muestra el mensaje de confirmacion"
    )
    void createQuestion() {
        QuestionDTO questionDTO = new QuestionDTO("1", "pregunta", "pregunta epica", Category.BACKEND, Difficulty.EASY, null);
        when(questionService.addQuestion(questionDTO)).thenReturn("Pregunta creada correctamente");

        ResponseEntity<String> response = questionController.createQuestion(questionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pregunta creada correctamente", response.getBody());

        System.out.println("Pregunta creada correctamente:");
        System.out.println("Status : " + response.getStatusCode());
        System.out.println("Mensaje: " + response.getBody());

        verify(questionService).addQuestion(questionDTO);
    }


    // CP10 - Normal: Obtener preguntas con filtros de categoria y dificultad
    @Test
    @CasoPrueba(
            id = "CP10",
            descripcion = "Obtener preguntas filtrando por categoria y dificultad",
            entrada = "category=DB, difficulty=EASY",
            tipo = "Normal",
            esperado = "Retorna la lista filtrada de preguntas"
    )
    void getQuestionsWithFilters() {
        QuestionDTO q1 = new QuestionDTO("1", "pregunta", "pregunta epica", Category.DB, Difficulty.EASY, null);
        QuestionDTO q2 = new QuestionDTO("2", "pregunta2", "pregunta epica 2", Category.DB, Difficulty.EASY, null);

        when(questionService.getQuestions(Category.DB, Difficulty.EASY, 0)).thenReturn(List.of(q1, q2));

        ResponseEntity<List<QuestionDTO>> response = questionController.getQuestions(Category.DB, Difficulty.EASY, 0);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        System.out.println("CP10 Preguntas obtenidas con filtros:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Cantidad de preguntas con categoria DB y dificultad EASY: " + response.getBody().size());

        verify(questionService).getQuestions(Category.DB, Difficulty.EASY, 0);
    }


    //CP11 - Normal: obtener pregunta por ID valido

    @Test
    @CasoPrueba(
            id = "CP11",
            descripcion = "Obtener pregunta existente por ID valido",
            entrada = "id=1",
            tipo = "Normal",
            esperado = "Retorna el QuestionDTO cpn el ID correspondiente"
    )
    void getQuestionById() {
        QuestionDTO expectedDTO = new QuestionDTO("1", "pregunta", "pregunta epica", Category.DB, Difficulty.EASY, null);
        when(questionService.getQuestionById("1")).thenReturn(expectedDTO);

        ResponseEntity<QuestionDTO> response = questionController.getQuestionById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Pregunta obtenida:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("ID: "+ response.getBody().getId());

        verify(questionService).getQuestionById("1");
    }


    //CP12 - Normal: Responder pregunta
    @Test
    @CasoPrueba(
            id = "CP12",
            descripcion = "Responder pregunta con usuario autenticado",
            entrada = "id=1, usuario=panini@devhub.com",
            tipo = "Normal",
            esperado = "Retorna el AnswerResponseDTO"
    )
    void answerQuestion() {
        AnswerDTO answerDTO = new AnswerDTO();
        AnswerResponseDTO expectedResponse = new AnswerResponseDTO();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("panini@devhub.com");
        when(questionService.verifyAnswer(answerDTO, "1", "panini@devhub.com"))
                .thenReturn(expectedResponse);

        ResponseEntity<AnswerResponseDTO> response = questionController.answer(
                "1", answerDTO, principal);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Respuesta verificada correctamente:");
        System.out.println("Status: " + response.getStatusCode());

        verify(questionService).verifyAnswer(answerDTO, "1", "panini@devhub.com");
    }


    // CP13 - Normal: Crear review valido
    @Test
    @CasoPrueba(
            id = "CP13",
            descripcion = "Crear review de una pregunta",
            entrada = "id=1, usuario=pambele@devhub.com",
            tipo= "Normal",
            esperado = "Retorna el mensaje de confirmacion de creación de Review"
    )
    void createReview() {
        ReviewDTO reviewDTO = new ReviewDTO();

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("pambele@devhub.com");
        when(questionService.createReview(reviewDTO, "pambele@devhub.com", "1"))
                .thenReturn("Review creada correctamente");

        ResponseEntity<String> response = questionController.createReview(
                "1", reviewDTO, principal);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Review creada correctamente", response.getBody());

        System.out.println("CP13 Review creada:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Mensaje de confirmación: " + response.getBody());

        verify(questionService).createReview(reviewDTO, "pambele@devhub.com", "1");
    }


    // CP14 - Normal: Obtener reviews asociadas a una pregunta
    @Test
    @CasoPrueba(
            id = "CP14",
            descripcion = "Obtener reviews de una pregunta por ID",
            entrada = "id=1, page=0",
            tipo = "Normal",
            esperado = "Retorna la lista de ReviewDTO"
    )
    void getReviewsByQuestionId() {
        ReviewDTO r1 = new ReviewDTO();
        ReviewDTO r2 = new ReviewDTO();
        when(questionService.getReviewsByQuestionId("1", 0))
                .thenReturn(List.of(r1, r2));

        ResponseEntity<List<ReviewDTO>> response = questionController.getReviewsByQuestionId(
                "1", 0);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        System.out.println("Reviews obtenidas:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Cantidad de reviews: " + response.getBody().size());

        verify(questionService).getReviewsByQuestionId("1", 0);
    }
}
