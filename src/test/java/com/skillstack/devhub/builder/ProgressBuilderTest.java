package com.skillstack.devhub.builder;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.model.Progress;
import com.skillstack.devhub.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class ProgressBuilderTest {

    @Mock
    private StatisticsService statisticsService;

    @Test
    @CasoPrueba(
            id = "CP58",
            descripcion = "ProgressBuilder - construye Progress con total y porcentaje correctos",
            entrada = "userId=user1, total=5, percentage=50.0",
            tipo = "Normal",
            esperado = "El objeto Progress retornado tiene totalAnswered=5 y percentage=50.0"
    )
    void progressBuilder_buildsTotalAndPercentage() {
        when(statisticsService.progress("TotalQuestionAnsweredStatistics", "user1")).thenReturn(5);
        when(statisticsService.progress("PercentageQuestionsAnsweredStatistics", "user1")).thenReturn(50.0);

        ProgressBuilder builder = new ProgressBuilder(statisticsService);
        builder.forUser("user1");
        builder.buildTotal();
        builder.buildPercentage();
        Progress result = builder.getResult();

        assertNotNull(result);
        assertEquals(5, result.getTotalAnswered());
        assertEquals(50.0, result.getPercentage());

        verify(statisticsService).progress("TotalQuestionAnsweredStatistics", "user1");
        verify(statisticsService).progress("PercentageQuestionsAnsweredStatistics", "user1");

        System.out.println("CP58 ProgressBuilder construido correctamente:");
        System.out.println("TotalAnswered: " + result.getTotalAnswered());
        System.out.println("Percentage: " + result.getPercentage());
    }

    @Test
    @CasoPrueba(
            id = "CP58B",
            descripcion = "ProgressBuilder - solo buildTotal sin buildPercentage",
            entrada = "userId=user2, total=3",
            tipo = "Normal",
            esperado = "El Progress tiene totalAnswered=3 y percentage=0.0 (valor por defecto)"
    )
    void progressBuilder_onlyTotal() {
        when(statisticsService.progress("TotalQuestionAnsweredStatistics", "user2")).thenReturn(3);

        ProgressBuilder builder = new ProgressBuilder(statisticsService);
        builder.forUser("user2");
        builder.buildTotal();
        Progress result = builder.getResult();

        assertNotNull(result);
        assertEquals(3, result.getTotalAnswered());
        assertEquals(0.0, result.getPercentage());

        verify(statisticsService).progress("TotalQuestionAnsweredStatistics", "user2");
        verify(statisticsService, never()).progress("PercentageQuestionsAnsweredStatistics", "user2");
    }

    @Test
    @CasoPrueba(
            id = "CP59",
            descripcion = "ProgressDirector - buildTotalAndPercentage delega correctamente al builder",
            entrada = "userId=user1, total=3, percentage=30.0",
            tipo = "Normal",
            esperado = "El Progress retornado tiene totalAnswered=3 y percentage=30.0"
    )
    void progressDirector_buildsTotalAndPercentage() {
        when(statisticsService.progress("TotalQuestionAnsweredStatistics", "user1")).thenReturn(3);
        when(statisticsService.progress("PercentageQuestionsAnsweredStatistics", "user1")).thenReturn(30.0);

        ProgressDirector director = new ProgressDirector(statisticsService);
        Progress result = director.buildTotalAndPercentage("user1");

        assertNotNull(result);
        assertEquals(3, result.getTotalAnswered());
        assertEquals(30.0, result.getPercentage());

        verify(statisticsService).progress("TotalQuestionAnsweredStatistics", "user1");
        verify(statisticsService).progress("PercentageQuestionsAnsweredStatistics", "user1");

        System.out.println("CP59 ProgressDirector construido correctamente:");
        System.out.println("TotalAnswered: " + result.getTotalAnswered());
        System.out.println("Percentage: " + result.getPercentage());
    }

    @Test
    @CasoPrueba(
            id = "CP59B",
            descripcion = "ProgressDirector - con 0 preguntas respondidas, porcentaje es 0.0",
            entrada = "userId=userNuevo, total=0, percentage=0.0",
            tipo = "Borde",
            esperado = "El Progress retornado tiene totalAnswered=0 y percentage=0.0"
    )
    void progressDirector_zeroAnswered() {
        when(statisticsService.progress("TotalQuestionAnsweredStatistics", "userNuevo")).thenReturn(0);
        when(statisticsService.progress("PercentageQuestionsAnsweredStatistics", "userNuevo")).thenReturn(0.0);

        ProgressDirector director = new ProgressDirector(statisticsService);
        Progress result = director.buildTotalAndPercentage("userNuevo");

        assertNotNull(result);
        assertEquals(0, result.getTotalAnswered());
        assertEquals(0.0, result.getPercentage());
    }
}
