package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.*;
import com.skillstack.devhub.exception.QuestionAlreadyExistsException;
import com.skillstack.devhub.exception.QuestionNotFoundException;
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

    // CP11 - Normal: Añadir Pregunta Válida
    @Test
    @CasoPrueba(
            id = "CP11",
            descripcion = "Añadir pregunta válida",
            entrada = "questionDTO = {69e005ee4d818c49ab701225, Patrón creacional, ¿Cuál de los siguientes es un patrón creacional?, BACKEND, EASY, options}",
            tipo = "Normal",
            esperado = "PREGUNTA CREADA EXITOSAMENTE"
    )
    void addQuestion_whenValidData(){
        OptionDTO optionCorrecta   = new OptionDTO("Builder", true);
        OptionDTO optionIncorrecta = new OptionDTO("Facade",  false);

        QuestionDTO questionDTO = new QuestionDTO(
                "69e005ee4d818c49ab701225",
                "Patrón creacional",
                "¿Cuál de los siguientes es un patrón creacional?",
                Category.BACKEND,
                Difficulty.EASY,
                List.of(optionCorrecta, optionIncorrecta)
        );

        when(questionRepository.findByTitle("Patrón creacional"))
                .thenReturn(Optional.empty());
        when(questionRepository.save(any(Question.class)))
                .thenReturn(new Question());

        String result = questionService.addQuestion(questionDTO);

        assertEquals("PREGUNTA CREADA EXITOSAMENTE", result);
        verify(questionRepository, times(1)).save(any(Question.class));
        System.out.println("CP11 — Resultado: " + result);
    }

    // CP12 - Normal: Buscar pregunta por Id
    @Test
    @CasoPrueba(
            id = "CP12",
            descripcion = "Buscar pregunta por Id",
            entrada = "id=69e005ee4d818c49ab701225",
            tipo = "Normal",
            esperado = "Se retorna un QuestionDTO con los datos de la pregunta encontrada"
    )
    void findQuestion(){
        Option option1   = new Option("Builder", true);
        Option option2 = new Option("Facade",  false);

        Question question = new Question(
                "Patrón creacional",
                "¿Cuál de los siguientes es un patrón creacional?",
                Category.BACKEND,
                Difficulty.EASY,
                List.of(option1, option2)
        );
        question.setId("69e005ee4d818c49ab701225");

        when(questionRepository.findById("69e005ee4d818c49ab701225")).thenReturn(Optional.of(question));

        QuestionDTO result = questionService.getQuestionById("69e005ee4d818c49ab701225");
        assertNotNull(result, "El QuestionDTO retornado no debe ser null");
        assertEquals("69e005ee4d818c49ab701225", result.getId(), "El ID debe coincidir con el buscado");
        assertEquals("Patrón creacional", result.getTitle(), "El titulo debe coincidir");
        assertEquals(Category.BACKEND, result.getCategory(), "La categoría debe coincidir");
        assertEquals(2, result.getOptions().size(), "Debe retornar ambas opciones");

        verify(questionRepository, times(1)).findById("69e005ee4d818c49ab701225");

        System.out.println("CP12 — ID encontrado: " + result.getId());
        System.out.println("Titulo pregunta: "+ result.getTitle());
    }

    // CP13 - Normal: Crear review válida
    @Test
    @CasoPrueba(
            id = "CP13",
            descripcion = "Crear review válida",
            entrada = "email= japan@test.com" +
                    "questionId=69e005ee4d818c49ab701225" +
                    "review DTO = {“Muy fácil”, 3}",
            tipo = "Normal",
            esperado = "REVIEW CREADO EXITOSAMENTE PARA (USUARIO) EN LA PREGUNTA 69e005ee4d818c49ab701225"
    )
    void addReview_whenValidData(){
        ReviewDTO reviewDTO = new ReviewDTO("Muy fácil", 3);
        Question question = new Question();
        question.setId("69e005ee4d818c49ab701225");
        User user = new User();
        user.setId("123");
        user.setEmail("japan@test.com");

        when(userRepository.findByEmail("japan@test.com")).thenReturn(Optional.of(user));
        when(questionRepository.findById("69e005ee4d818c49ab701225")).thenReturn(Optional.of(question));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());

        String result = questionService.createReview(reviewDTO, "japan@test.com", "69e005ee4d818c49ab701225");

        assertEquals("REVIEW CREADO EXITOSAMENTE PARA 123 EN LA PREGUNTA 69e005ee4d818c49ab701225", result);
        verify(userRepository, times (1)).findByEmail("japan@test.com");
        verify(questionRepository, times(1)).findById("69e005ee4d818c49ab701225");
        verify(reviewRepository, times(1)).save(any(Review.class));

        System.out.println("CP13 - Resultado: "+ result);
    }

    // CP14 - Normal: Verificar pregunta con respuesta correcta
    @Test
    @CasoPrueba(
        id          = "CP14",
        descripcion = "Verificar pregunta con respuesta correcta",
        entrada     = "questionId=69e005ee4d818c49ab701225, userEmail=verify@test.com, selectedOption=Contenedor",
        tipo        = "Normal",
        esperado    = "Se retorna un AnswerResponseDTO con isCorrect = true y totalScore incrementado 1 punto (dificultad EASY)"
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
        esperado    = "Se retorna una lista de ReviewDTO con los campos comment y rating de cada review asociado a la pregunta"
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

    // CP16 - Negativa: Añadir pregunta ya existente
    @Test
    @CasoPrueba(
            id = "CP16",
            descripcion = "Añadir pregunta ya existente",
            entrada = "questionDTO = {69e005ee4d818c49ab701225, Patrón creacional, ¿Cuál de los siguientes es un patrón creacional?, BACKEND, EASY, options}",
            tipo = "Negativa",
            esperado = "QuestionAlreadyExistsException"
    )
    void addQuestion_whenQuestionAlreadyExists(){
        OptionDTO option1   = new OptionDTO("Builder", true);
        OptionDTO option2 = new OptionDTO("Facade",  false);
        QuestionDTO questionDTO = new QuestionDTO(
                "69e005ee4d818c49ab701225",
                "Patrón creacional",
                "¿Cuál de los siguientes es un patrón creacional?",
                Category.BACKEND,
                Difficulty.EASY,
                List.of(option1, option2)
        );

        when(questionRepository.findByTitle("Patrón creacional")).thenReturn(Optional.of(new Question()));

        assertThrows(QuestionAlreadyExistsException.class, ()-> questionService.addQuestion(questionDTO),
                "Debe lanzar QuestionAlreadyExistsException si la pregunta ya existe");
        verify(questionRepository, never()).save(any(Question.class));

        System.out.println("CP16 — Excepción lanzada: QuestionAlreadyExistsException");
    }

    // CP17 - Negativa: Buscar pregunta con id no existente
    @Test
    @CasoPrueba(
            id = "CP17",
            descripcion = "Buscar pregunta con id no existente",
            entrada = "id=1",
            tipo = "Negativa",
            esperado = "QuestionNotFoundException"
    )
    void findQuestion_whenIdDoesNotExists(){
        when(questionRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, ()->questionService.getQuestionById("1"),
                "Debe lanzar QuestionNotFoundException si el id no se encuentra");
        verify(questionRepository, times(1)).findById("1");

        System.out.println("CP17 — Excepción lanzada: QuestionNotFoundException");
    }

    // CP18 - Borde: Crear review utilizando rating máximo
    @Test
    @CasoPrueba(
            id          = "CP18",
            descripcion = "Crear review utilizando rating máximo",
            entrada     = "email=japan@test.com, questionId=69e005ee4d818c49ab701225, reviewDTO = {“Excelente”, 5}",
            tipo        = "Borde",
            esperado    = "REVIEW CREADO EXITOSAMENTE PARA (USUARIO) EN LA PREGUNTA 69e005ee4d818c49ab701225"
    )
    void createReview_whenMaximumRating(){
        ReviewDTO reviewDTO = new ReviewDTO("Excelente", 5);
        Question question = new Question();
        question.setId("69e005ee4d818c49ab701225");
        User user = new User();
        user.setId("123");
        user.setEmail("japan@test.com");

        when(userRepository.findByEmail("japan@test.com")).thenReturn(Optional.of(user));
        when(questionRepository.findById("69e005ee4d818c49ab701225")).thenReturn(Optional.of(question));
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review());

        String result = questionService.createReview(reviewDTO, "japan@test.com", "69e005ee4d818c49ab701225");

        assertEquals("REVIEW CREADO EXITOSAMENTE PARA 123 EN LA PREGUNTA 69e005ee4d818c49ab701225", result);
        verify(userRepository, times (1)).findByEmail("japan@test.com");
        verify(questionRepository, times(1)).findById("69e005ee4d818c49ab701225");
        verify(reviewRepository, times(1)).save(any(Review.class));

        System.out.println("CP18 - Resultado: "+ result);
    }

    // CP19 - Borde: Crear review utilizando rating mínimo
    @Test
    @CasoPrueba(
        id          = "CP19",
        descripcion = "Crear review utilizando rating mínimo",
        entrada     = "questionId=69e005ee4d818c49ab701225, userEmail=verify@test.com, reviewDTO={Imposible, 1}",
        tipo        = "Borde",
        esperado    = "REVIEW CREADO EXITOSAMENTE PARA (USUARIO) EN LA PREGUNTA 69e005ee4d818c49ab701225"
    )
    void createReview_whenMinimumRating_createsReviewSuccessfully() {

        String questionId = "69e005ee4d818c49ab701225";

        User userFake = new User();
        userFake.setId("123");
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

        String expectedMessage = "REVIEW CREADO CORRECTAMENTE PARA USUARIO "
                + userFake.getId()
                + " EN LA PREGUNTA " + questionId;

        String result = questionService.createReview(reviewDTO, userFake.getEmail(), questionId);

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
        esperado    = "Se retorna un AnswerResponseDTO con isCorrect = false y totalScore sin cambios"
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
