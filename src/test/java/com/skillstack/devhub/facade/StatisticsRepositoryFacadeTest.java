package com.skillstack.devhub.facade;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.exception.QuestionNotFoundException;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.repository.AnswerRepository;
import com.skillstack.devhub.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class StatisticsRepositoryFacadeTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private StatisticsRepositoryFacade facade;

    @Test
    @CasoPrueba(
            id = "CP75",
            descripcion = "StatisticsRepositoryFacade - findAnsweredQuestionsByUser retorna lista de IDs",
            entrada = "userId=user1",
            tipo = "Normal",
            esperado = "Retorna lista con los IDs de preguntas respondidas por el usuario"
    )
    void findAnsweredQuestionsByUser_returnsIds() {
        when(answerRepository.findDistinctQuestionIdByUserId("user1"))
                .thenReturn(List.of("q1", "q2", "q3"));

        List<String> result = facade.findAnsweredQuestionsByUser("user1");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("q1", result.get(0));
        assertEquals("q2", result.get(1));
        assertEquals("q3", result.get(2));

        verify(answerRepository).findDistinctQuestionIdByUserId("user1");

        System.out.println("CP75 findAnsweredQuestionsByUser retorno " + result.size() + " preguntas");
    }

    @Test
    @CasoPrueba(
            id = "CP75B",
            descripcion = "StatisticsRepositoryFacade - findAnsweredQuestionsByUser retorna lista vacia si el usuario no ha respondido",
            entrada = "userId=userSinRespuestas",
            tipo = "Borde",
            esperado = "Retorna lista vacia"
    )
    void findAnsweredQuestionsByUser_emptyList() {
        when(answerRepository.findDistinctQuestionIdByUserId("userSinRespuestas"))
                .thenReturn(List.of());

        List<String> result = facade.findAnsweredQuestionsByUser("userSinRespuestas");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @CasoPrueba(
            id = "CP76",
            descripcion = "StatisticsRepositoryFacade - findQuestionById retorna la pregunta correcta",
            entrada = "questionId=q1, title=Que es Java?",
            tipo = "Normal",
            esperado = "Retorna la Question con el ID y titulo correctos"
    )
    void findQuestionById_found() {
        Question q = new Question();
        q.setId("q1");
        q.setTitle("Que es Java?");

        when(questionRepository.findById("q1")).thenReturn(Optional.of(q));

        Question result = facade.findQuestionById("q1");

        assertNotNull(result);
        assertEquals("q1", result.getId());
        assertEquals("Que es Java?", result.getTitle());

        verify(questionRepository).findById("q1");

        System.out.println("CP76 findQuestionById encontro: " + result.getTitle());
    }

    @Test
    @CasoPrueba(
            id = "CP77",
            descripcion = "StatisticsRepositoryFacade - findQuestionById lanza QuestionNotFoundException si no existe",
            entrada = "questionId=noExiste",
            tipo = "Negativa",
            esperado = "Lanza QuestionNotFoundException con mensaje de ID no encontrado"
    )
    void findQuestionById_notFound_throwsException() {
        when(questionRepository.findById("noExiste")).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class,
                () -> facade.findQuestionById("noExiste"));

        verify(questionRepository).findById("noExiste");

        System.out.println("CP77 QuestionNotFoundException lanzada correctamente");
    }

    @Test
    @CasoPrueba(
            id = "CP78",
            descripcion = "StatisticsRepositoryFacade - countTotalQuestions retorna el total de preguntas",
            entrada = "total=10 preguntas en la base de datos",
            tipo = "Normal",
            esperado = "Retorna 10 como total de preguntas"
    )
    void countTotalQuestions_returnsCount() {
        when(questionRepository.count()).thenReturn(10L);

        long total = facade.countTotalQuestions();

        assertEquals(10L, total);
        verify(questionRepository).count();

        System.out.println("CP78 countTotalQuestions retorno: " + total);
    }

    @Test
    @CasoPrueba(
            id = "CP78B",
            descripcion = "StatisticsRepositoryFacade - countTotalQuestions retorna 0 si no hay preguntas",
            entrada = "total=0 preguntas",
            tipo = "Borde",
            esperado = "Retorna 0"
    )
    void countTotalQuestions_returnsZeroWhenEmpty() {
        when(questionRepository.count()).thenReturn(0L);

        long total = facade.countTotalQuestions();

        assertEquals(0L, total);
    }
}
