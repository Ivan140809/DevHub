package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.QuestionDTO;
import com.skillstack.devhub.facade.StatisticsRepositoryFacade;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import com.skillstack.devhub.model.Question;
import com.skillstack.devhub.strategy.AnsweredQuestionsStatistic;
import com.skillstack.devhub.strategy.PercentageQuestionsAnsweredStatistics;
import com.skillstack.devhub.strategy.Statistics;
import com.skillstack.devhub.strategy.TotalQuestionsAnsweredStatistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class StatisticsTest {

    @Mock
    private StatisticsRepositoryFacade statisticsRepositoryFacade;

    @InjectMocks
    private AnsweredQuestionsStatistic answeredQuestionsStatistic;

    @InjectMocks
    private PercentageQuestionsAnsweredStatistics percentageQuestionsAnsweredStatistics;

    @InjectMocks
    private TotalQuestionsAnsweredStatistics totalQuestionsAnsweredStatistics;


    // CP31 - Normal: Obtener preguntas respondidas por usuario

    @Test
    @CasoPrueba(
            id = "CP31",
            descripcion = "Obtener preguntas respondidas por un usuario",
            entrada = "userId=1",
            tipo = "Normal",
            esperado = "Retorna lista de 2 QuestionDTO con los datos de las preguntas"
    )
    void answeredQuestionsProgress() {
        Question q1 = new Question();
        q1.setId("q1");
        q1.setTitle("prueba");
        q1.setCategory(Category.BACKEND);
        q1.setDifficulty(Difficulty.EASY);
        Question q2 = new Question();
        q2.setId("q2");
        q2.setTitle("prueba 2");
        q2.setCategory(Category.FRONTEND);
        q2.setDifficulty(Difficulty.MEDIUM);

        when(statisticsRepositoryFacade.findAnsweredQuestionsByUser("1"))
                .thenReturn(List.of("q1", "q2"));
        when(statisticsRepositoryFacade.findQuestionById("q1")).thenReturn(q1);
        when(statisticsRepositoryFacade.findQuestionById("q2")).thenReturn(q2);

        List<QuestionDTO> result = answeredQuestionsStatistic.progress("1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("q1", result.get(0).getId());
        assertEquals("prueba", result.get(0).getTitle());
        assertEquals("q2", result.get(1).getId());
        assertEquals("prueba 2", result.get(1).getTitle());

        System.out.println("CP31 Preguntas respondidas obtenidas correctamente:");
        System.out.println("Cantidad: " + result.size());
        for (int i=0; i<result.size(); i++){
            System.out.println("ID: " + result.get(i).getId());
            System.out.println("Titulo: " + result.get(i).getTitle());
        }


        verify(statisticsRepositoryFacade).findAnsweredQuestionsByUser("1");
        verify(statisticsRepositoryFacade).findQuestionById("q1");
        verify(statisticsRepositoryFacade).findQuestionById("q2");
    }


    // CP32 - Normal: Calcular porcentaje de preguntas respondidas

    @Test
    @CasoPrueba(
            id = "CP32",
            descripcion = "Calcular porcentaje de preguntas respondidas por un usuario",
            entrada = "userId=1, total=10, respondidas=5",
            tipo = "Normal",
            esperado = "Retorna 50.0 como porcentaje"
    )
    void percentageProgress() {
        when(statisticsRepositoryFacade.countTotalQuestions()).thenReturn((long) 10);
        when(statisticsRepositoryFacade.findAnsweredQuestionsByUser("1"))
                .thenReturn(List.of("q1", "q2", "q3", "q4", "q5"));

        Double result = percentageQuestionsAnsweredStatistics.progress("1");

        assertNotNull(result);
        assertEquals(50.0, result);

        System.out.println("CP32 Porcentaje calculado correctamente:");
        System.out.println("Porcentaje: "+ result);

        verify(statisticsRepositoryFacade).countTotalQuestions();
        verify(statisticsRepositoryFacade).findAnsweredQuestionsByUser("1");
    }


    // CP33 - Normal: Obtener total de preguntas respondidas

    @Test
    @CasoPrueba(
            id = "CP33",
            descripcion = "Obtener total de preguntas respondidas por un usuario",
            entrada = "userId=1",
            tipo = "Normal",
            esperado = "Retorna 3 como total de preguntas respondidas"
    )
    void totalQuestionsAnsweredProgress() {
        when(statisticsRepositoryFacade.findAnsweredQuestionsByUser("1"))
                .thenReturn(List.of("q1", "q2", "q3"));

        Integer result = totalQuestionsAnsweredStatistics.progress("1");

        assertNotNull(result);
        assertEquals(3, result);

        System.out.println("CP33 Total de preguntas respondidas:");
        System.out.println("Total: " + result);

        verify(statisticsRepositoryFacade).findAnsweredQuestionsByUser("1");
    }


    // CP34 - Normal: StatisticsService delega la estrategia segun nombre

    @Test
    @CasoPrueba(
            id = "CP34",
            descripcion = "StatisticsService delega correctamente a la estrategia por nombre",
            entrada = "strategyName=TotalQuestionsAnsweredStatistics, userId=1",
            tipo = "Normal",
            esperado = "Retorna el resultado de la estrategia TotalQuestionsAnsweredStatistics"
    )
    void statisticsServiceDelegatesToCorrectStrategy() {
        when(statisticsRepositoryFacade.findAnsweredQuestionsByUser("1"))
                .thenReturn(List.of("q1", "q2"));

        List<Statistics<?>> strategies = List.of(totalQuestionsAnsweredStatistics, answeredQuestionsStatistic, percentageQuestionsAnsweredStatistics);
        StatisticsService statisticsService = new StatisticsService(strategies);

        Integer result = statisticsService.progress("TotalQuestionsAnsweredStatistics", "1");

        assertNotNull(result);
        assertEquals(2, result);

        System.out.println("CP34 StatisticsService delego TotalQuestionsAnsweredStatistics:");
        System.out.println("Total: " + result);

        verify(statisticsRepositoryFacade).findAnsweredQuestionsByUser("1");
    }
}
