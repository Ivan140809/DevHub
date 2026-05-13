package com.skillstack.devhub.controller;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.builder.ProgressDirector;
import com.skillstack.devhub.dto.ProgressDTO;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.model.Progress;
import com.skillstack.devhub.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class StatisticsControllerTest {

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private ProgressDirector progressDirector;

    @InjectMocks
    private StatisticsController statisticsController;

    @Test
    @CasoPrueba(
            id = "CP83",
            descripcion = "StatisticsController - getAnsweredQuestions retorna 200 con lista de preguntas respondidas",
            entrada = "userId=test@test.com",
            tipo = "Normal",
            esperado = "Retorna 200 OK con lista de 2 QuestionDTO"
    )
    void getAnsweredQuestions_returnsOk() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        QuestionDTO q1 = mock(QuestionDTO.class);
        QuestionDTO q2 = mock(QuestionDTO.class);
        when(statisticsService.progress("AnsweredQuestionsStatistic", "test@test.com"))
                .thenReturn(List.of(q1, q2));

        ResponseEntity<List<QuestionDTO>> response = statisticsController.getAnsweredQuestions(auth);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(statisticsService).progress("AnsweredQuestionsStatistic", "test@test.com");

        System.out.println("CP83 getAnsweredQuestions retorno " + response.getBody().size() + " preguntas");
    }

    @Test
    @CasoPrueba(
            id = "CP83B",
            descripcion = "StatisticsController - getAnsweredQuestions con lista vacia retorna 200",
            entrada = "userId=nuevoUsuario@test.com (sin preguntas respondidas)",
            tipo = "Borde",
            esperado = "Retorna 200 OK con lista vacia"
    )
    void getAnsweredQuestions_emptyList_returnsOk() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("nuevoUsuario@test.com");

        when(statisticsService.progress("AnsweredQuestionsStatistic", "nuevoUsuario@test.com"))
                .thenReturn(List.of());

        ResponseEntity<List<QuestionDTO>> response = statisticsController.getAnsweredQuestions(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @CasoPrueba(
            id = "CP84",
            descripcion = "StatisticsController - getProgress retorna 200 con ProgressDTO del usuario",
            entrada = "userId=test@test.com, totalAnswered=5, percentage=50.0",
            tipo = "Normal",
            esperado = "Retorna 200 OK con ProgressDTO con los datos del progreso"
    )
    void getProgress_returnsOk() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@test.com");

        Progress progress = new Progress();
        progress.setTotalAnswered(5);
        progress.setPercentage(50.0);

        when(progressDirector.buildTotalAndPercentage("test@test.com")).thenReturn(progress);

        ResponseEntity<ProgressDTO> response = statisticsController.getProgress(auth);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(progressDirector).buildTotalAndPercentage("test@test.com");

        System.out.println("CP84 getProgress retorno correctamente");
    }

    @Test
    @CasoPrueba(
            id = "CP84B",
            descripcion = "StatisticsController - getProgress con porcentaje 100% retorna 200",
            entrada = "userId=expert@test.com, totalAnswered=10, percentage=100.0",
            tipo = "Borde",
            esperado = "Retorna 200 OK con ProgressDTO de 100% completado"
    )
    void getProgress_fullCompletion_returnsOk() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("expert@test.com");

        Progress progress = new Progress();
        progress.setTotalAnswered(10);
        progress.setPercentage(100.0);

        when(progressDirector.buildTotalAndPercentage("expert@test.com")).thenReturn(progress);

        ResponseEntity<ProgressDTO> response = statisticsController.getProgress(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
