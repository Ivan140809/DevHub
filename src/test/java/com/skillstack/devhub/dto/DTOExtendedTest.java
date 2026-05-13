package com.skillstack.devhub.dto;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class DTOExtendedTest {

    // ======================== RankingDTO ========================

    @Test
    @CasoPrueba(
            id = "CP200",
            descripcion = "RankingDTO - constructor vacío y setters/getters de todos los campos",
            entrada = "position=1, username=pepe, email=pepe@test.com",
            tipo = "Normal",
            esperado = "Todos los getters retornan los valores seteados"
    )
    void rankingDTO_emptyConstructorAndSetters() {
        RankingDTO dto = new RankingDTO();
        dto.setPosition(1);
        dto.setUsername("pepe");
        dto.setEmail("pepe@test.com");

        assertEquals(1, dto.getPosition());
        assertEquals("pepe", dto.getUsername());
        assertEquals("pepe@test.com", dto.getEmail());
    }

    @Test
    @CasoPrueba(
            id = "CP201",
            descripcion = "RankingDTO - constructor completo con todos los parámetros",
            entrada = "position=2, username=juan, email=juan@test.com, totalScore=150",
            tipo = "Normal",
            esperado = "Todos los getters retornan los valores del constructor"
    )
    void rankingDTO_fullConstructor() {
        RankingDTO dto = new RankingDTO(2, "juan", "juan@test.com", 150);
        assertEquals(2, dto.getPosition());
        assertEquals("juan", dto.getUsername());
        assertEquals("juan@test.com", dto.getEmail());
    }

    @Test
    @CasoPrueba(
            id = "CP202",
            descripcion = "RankingDTO - posición borde mínima = 1",
            entrada = "position=1",
            tipo = "Borde",
            esperado = "getPosition retorna 1"
    )
    void rankingDTO_positionBorderMin() {
        RankingDTO dto = new RankingDTO(1, "user", "user@test.com", 0);
        assertEquals(1, dto.getPosition());
    }

    // ======================== UserResponseDTO ========================

    @Test
    @CasoPrueba(
            id = "CP203",
            descripcion = "UserResponseDTO - constructor de 9 argumentos asigna todos los campos",
            entrada = "id, firstName, lastName, username, email, phone, preferences, answeredQuestions, totalScore",
            tipo = "Normal",
            esperado = "Todos los getters retornan los valores correctos y getRole es null"
    )
    void userResponseDTO_9argsConstructor() {
        UserResponseDTO dto = new UserResponseDTO(
                "id1", "Ana", "Gomez", "anag", "ana@test.com", "123",
                List.of("Java", "Python"), 5, 100
        );
        assertEquals("id1", dto.getId());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Gomez", dto.getLastName());
        assertEquals("anag", dto.getUsername());
        assertEquals("ana@test.com", dto.getEmail());
        assertEquals("123", dto.getPhone());
        assertEquals(2, dto.getPreferences().size());
        assertEquals(5, dto.getAnsweredQuestions());
        assertEquals(100, dto.getTotalScore());
        assertNull(dto.getRole());
    }

    @Test
    @CasoPrueba(
            id = "CP204",
            descripcion = "UserResponseDTO - constructor de 10 argumentos asigna el role correctamente",
            entrada = "...role=ADMIN",
            tipo = "Normal",
            esperado = "getRole retorna 'ADMIN'"
    )
    void userResponseDTO_10argsConstructor() {
        UserResponseDTO dto = new UserResponseDTO(
                "id1", "Ana", "Gomez", "anag", "ana@test.com", "123",
                List.of(), 5, 100, "ADMIN"
        );
        assertEquals("ADMIN", dto.getRole());
        assertEquals("id1", dto.getId());
    }

    @Test
    @CasoPrueba(
            id = "CP205",
            descripcion = "UserResponseDTO - setters vacío actualizan todos los campos",
            entrada = "Todos los campos por setter",
            tipo = "Normal",
            esperado = "Los getters reflejan los nuevos valores tras los setters"
    )
    void userResponseDTO_setters() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId("id2");
        dto.setFirstName("Carlos");
        dto.setLastName("Ruiz");
        dto.setUsername("cruiz");
        dto.setEmail("carlos@test.com");
        dto.setPhone("456");
        dto.setRole("USER");
        dto.setTotalQuestions(20);

        assertEquals("id2", dto.getId());
        assertEquals("Carlos", dto.getFirstName());
        assertEquals("Ruiz", dto.getLastName());
        assertEquals("cruiz", dto.getUsername());
        assertEquals("carlos@test.com", dto.getEmail());
        assertEquals("456", dto.getPhone());
        assertEquals("USER", dto.getRole());
        assertEquals(20, dto.getTotalQuestions());
    }

    @Test
    @CasoPrueba(
            id = "CP206",
            descripcion = "UserResponseDTO - getTotalQuestions retorna 0 por defecto en constructor vacío",
            entrada = "Sin parámetros",
            tipo = "Normal",
            esperado = "getTotalQuestions retorna 0"
    )
    void userResponseDTO_getTotalQuestionsDefault() {
        UserResponseDTO dto = new UserResponseDTO();
        assertEquals(0, dto.getTotalQuestions());
    }

    // ======================== QuestionDTO ========================

    @Test
    @CasoPrueba(
            id = "CP207",
            descripcion = "QuestionDTO - constructor completo asigna todos los campos",
            entrada = "id=q1, title=Titulo, statement=Enunciado, category=BACKEND, difficulty=EASY, options",
            tipo = "Normal",
            esperado = "Todos los getters retornan los valores del constructor"
    )
    void questionDTO_fullConstructor() {
        List<OptionDTO> options = List.of(
                new OptionDTO("Opcion A", true),
                new OptionDTO("Opcion B", false)
        );
        QuestionDTO dto = new QuestionDTO("q1", "Titulo", "Enunciado", Category.BACKEND, Difficulty.EASY, options);

        assertEquals("q1", dto.getId());
        assertEquals("Titulo", dto.getTitle());
        assertEquals("Enunciado", dto.getStatement());
        assertEquals(Category.BACKEND, dto.getCategory());
        assertEquals(Difficulty.EASY, dto.getDifficulty());
        assertEquals(2, dto.getOptions().size());
    }

    @Test
    @CasoPrueba(
            id = "CP208",
            descripcion = "QuestionDTO - setters actualizan id, title, category y difficulty",
            entrada = "Nuevos valores para todos los campos",
            tipo = "Normal",
            esperado = "Los getters reflejan los nuevos valores"
    )
    void questionDTO_setters() {
        QuestionDTO dto = new QuestionDTO("q1", "Titulo", "Enunciado", Category.BACKEND, Difficulty.EASY,
                List.of(new OptionDTO("A", true)));
        dto.setId("q2");
        dto.setTitle("Nuevo titulo");
        dto.setCategory(Category.FRONTEND);
        dto.setDifficulty(Difficulty.HARD);

        assertEquals("q2", dto.getId());
        assertEquals("Nuevo titulo", dto.getTitle());
        assertEquals(Category.FRONTEND, dto.getCategory());
        assertEquals(Difficulty.HARD, dto.getDifficulty());
    }

    @Test
    @CasoPrueba(
            id = "CP209",
            descripcion = "QuestionDTO - category DB y difficulty MEDIUM",
            entrada = "category=DB, difficulty=MEDIUM",
            tipo = "Normal",
            esperado = "getCategory y getDifficulty retornan los valores correctos"
    )
    void questionDTO_dbMedium() {
        QuestionDTO dto = new QuestionDTO("q1", "T", "E", Category.DB, Difficulty.MEDIUM,
                List.of(new OptionDTO("X", true)));
        assertEquals(Category.DB, dto.getCategory());
        assertEquals(Difficulty.MEDIUM, dto.getDifficulty());
    }

    // ======================== UserRegisterDTO ========================

    @Test
    @CasoPrueba(
            id = "CP210",
            descripcion = "UserRegisterDTO - constructor completo asigna todos los campos",
            entrada = "firstName=Ivan, lastName=Lastra, username=ilastra, email=ivan@test.com, password=Pass1!, phone=123",
            tipo = "Normal",
            esperado = "Todos los getters retornan los valores del constructor"
    )
    void userRegisterDTO_fullConstructor() {
        UserRegisterDTO dto = new UserRegisterDTO("Ivan", "Lastra", "ilastra", "ivan@test.com", "Pass1!", "123");

        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Lastra", dto.getLastName());
        assertEquals("ilastra", dto.getUsername());
        assertEquals("ivan@test.com", dto.getEmail());
        assertEquals("Pass1!", dto.getPassword());
        assertEquals("123", dto.getPhone());
    }

    @Test
    @CasoPrueba(
            id = "CP211",
            descripcion = "UserRegisterDTO - setters actualizan todos los campos correctamente",
            entrada = "Valores por setter",
            tipo = "Normal",
            esperado = "Los getters reflejan los valores actualizados"
    )
    void userRegisterDTO_setters() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setFirstName("Laura");
        dto.setLastName("Torres");
        dto.setUsername("ltorres");
        dto.setEmail("laura@test.com");
        dto.setPassword("NewPass1!");
        dto.setPhone("789");

        assertEquals("Laura", dto.getFirstName());
        assertEquals("Torres", dto.getLastName());
        assertEquals("ltorres", dto.getUsername());
        assertEquals("laura@test.com", dto.getEmail());
        assertEquals("NewPass1!", dto.getPassword());
        assertEquals("789", dto.getPhone());
    }

    // ======================== ReviewDTO ========================

    @Test
    @CasoPrueba(
            id = "CP212",
            descripcion = "ReviewDTO - constructor vacío crea objeto sin lanzar excepción",
            entrada = "Sin parámetros",
            tipo = "Normal",
            esperado = "El objeto ReviewDTO no es null y rating es 0"
    )
    void reviewDTO_emptyConstructor() {
        ReviewDTO dto = new ReviewDTO();
        assertNotNull(dto);
        assertEquals(0, dto.getRating());
        assertNull(dto.getComment());
    }

    @Test
    @CasoPrueba(
            id = "CP213",
            descripcion = "ReviewDTO - setComment actualiza el comentario",
            entrada = "comment inicial=Original, nuevo=Editado",
            tipo = "Normal",
            esperado = "getComment retorna 'Editado' tras el setComment"
    )
    void reviewDTO_setComment() {
        ReviewDTO dto = new ReviewDTO("Original", 3);
        dto.setComment("Editado");
        assertEquals("Editado", dto.getComment());
    }

    @Test
    @CasoPrueba(
            id = "CP214",
            descripcion = "ReviewDTO - rating mínimo=1 y máximo=5 se asignan correctamente",
            entrada = "rating=1 y rating=5",
            tipo = "Borde",
            esperado = "getRating retorna 1 y 5 respectivamente"
    )
    void reviewDTO_borderRatings() {
        ReviewDTO dtoMin = new ReviewDTO("Malo", 1);
        ReviewDTO dtoMax = new ReviewDTO("Excelente", 5);

        assertEquals(1, dtoMin.getRating());
        assertEquals(5, dtoMax.getRating());
    }

    // ======================== OptionDTO ========================

    @Test
    @CasoPrueba(
            id = "CP215",
            descripcion = "OptionDTO - constructor asigna text correctamente y toOption genera Option válido",
            entrada = "text=Builder, correct=true",
            tipo = "Normal",
            esperado = "getText retorna 'Builder' y toOption retorna Option con mismo text"
    )
    void optionDTO_constructorAndToOption() {
        OptionDTO dto = new OptionDTO("Builder", true);
        assertEquals("Builder", dto.getText());
        assertNotNull(dto.toOption());
        assertEquals("Builder", dto.toOption().getText());
        assertTrue(dto.toOption().isCorrect());
    }
}
