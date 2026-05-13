package com.skillstack.devhub.model;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class ModelTest {

    // ======================== Answer ========================

    @Test
    @CasoPrueba(
            id = "CP100",
            descripcion = "Answer - constructor asigna campos y getUserId retorna el valor correcto",
            entrada = "questionId=q1, selectedOption=A, userId=u1, timer=PT10S",
            tipo = "Normal",
            esperado = "getUserId retorna 'u1', getId retorna null"
    )
    void answer_constructorAndGetters() {
        Duration timer = Duration.ofSeconds(10);
        Answer answer = new Answer("q1", "A", "u1", timer);
        assertEquals("u1", answer.getUserId());
        assertNull(answer.getId());
    }

    @Test
    @CasoPrueba(
            id = "CP101",
            descripcion = "Answer - setUserId actualiza el campo userId correctamente",
            entrada = "userId inicial=u1, nuevo userId=newUser",
            tipo = "Normal",
            esperado = "getUserId retorna 'newUser' tras el setUserId"
    )
    void answer_setUserId() {
        Answer answer = new Answer("q1", "A", "u1", Duration.ofSeconds(5));
        answer.setUserId("newUser");
        assertEquals("newUser", answer.getUserId());
    }

    // ======================== Option ========================

    @Test
    @CasoPrueba(
            id = "CP102",
            descripcion = "Option - constructor vacío crea objeto con campos en null",
            entrada = "Sin parámetros",
            tipo = "Normal",
            esperado = "getText retorna null, isCorrect retorna null"
    )
    void option_emptyConstructor() {
        Option option = new Option();
        assertNotNull(option);
        assertNull(option.getText());
        assertNull(option.isCorrect());
    }

    @Test
    @CasoPrueba(
            id = "CP103",
            descripcion = "Option - constructor completo asigna text y correct",
            entrada = "text=Builder, correct=true",
            tipo = "Normal",
            esperado = "getText retorna 'Builder', isCorrect retorna true"
    )
    void option_fullConstructorCorrect() {
        Option option = new Option("Builder", true);
        assertEquals("Builder", option.getText());
        assertTrue(option.isCorrect());
    }

    @Test
    @CasoPrueba(
            id = "CP104",
            descripcion = "Option - constructor con correct=false e isCorrect retorna false",
            entrada = "text=Facade, correct=false",
            tipo = "Normal",
            esperado = "isCorrect retorna false"
    )
    void option_isCorrectFalse() {
        Option option = new Option("Facade", false);
        assertEquals("Facade", option.getText());
        assertFalse(option.isCorrect());
    }

    @Test
    @CasoPrueba(
            id = "CP105",
            descripcion = "Option - setText actualiza el texto de la opción",
            entrada = "text inicial=Builder, nuevo text=Singleton",
            tipo = "Normal",
            esperado = "getText retorna 'Singleton' tras el setText"
    )
    void option_setText() {
        Option option = new Option("Builder", false);
        option.setText("Singleton");
        assertEquals("Singleton", option.getText());
    }

    // ======================== Review ========================

    @Test
    @CasoPrueba(
            id = "CP106",
            descripcion = "Review - constructor vacío crea objeto sin lanzar excepción",
            entrada = "Sin parámetros",
            tipo = "Normal",
            esperado = "El objeto Review no es null"
    )
    void review_emptyConstructor() {
        Review review = new Review();
        assertNotNull(review);
    }

    @Test
    @CasoPrueba(
            id = "CP107",
            descripcion = "Review - constructor completo asigna comment, rating y userId correctamente",
            entrada = "comment=Muy buena, rating=4, questionId=q1, userId=u1, date=hoy",
            tipo = "Normal",
            esperado = "getComment, getRating y getUserId retornan los valores asignados"
    )
    void review_fullConstructor() {
        LocalDate today = LocalDate.now();
        Review review = new Review("Muy buena", 4, "q1", "u1", today);
        assertEquals("Muy buena", review.getComment());
        assertEquals(4, review.getRating());
        assertEquals("u1", review.getUserId());
    }

    @Test
    @CasoPrueba(
            id = "CP108",
            descripcion = "Review - setters actualizan todos los campos correctamente",
            entrada = "comment=Editado, rating=5, userId=u2, id=rev-1",
            tipo = "Normal",
            esperado = "Los getters reflejan los valores seteados"
    )
    void review_setters() {
        Review review = new Review();
        review.setId("rev-1");
        review.setComment("Editado");
        review.setRating(5);
        review.setUserId("u2");

        assertEquals("rev-1", review.getId());
        assertEquals("Editado", review.getComment());
        assertEquals(5, review.getRating());
        assertEquals("u2", review.getUserId());
    }

    // ======================== CommentReaction ========================

    @Test
    @CasoPrueba(
            id = "CP109",
            descripcion = "CommentReaction - constructor HAPPYFACE asigna reaction y userId",
            entrada = "reaction=HAPPYFACE, commentId=c1, userId=u1",
            tipo = "Normal",
            esperado = "getReaction retorna HAPPYFACE, getUserId retorna 'u1'"
    )
    void commentReaction_happyface() {
        CommentReaction reaction = new CommentReaction(Reaction.HAPPYFACE, "c1", "u1");
        assertEquals(Reaction.HAPPYFACE, reaction.getReaction());
        assertEquals("u1", reaction.getUserId());
    }

    @Test
    @CasoPrueba(
            id = "CP110",
            descripcion = "CommentReaction - constructor SADFACE asigna reaction correctamente",
            entrada = "reaction=SADFACE, commentId=c1, userId=u2",
            tipo = "Normal",
            esperado = "getReaction retorna SADFACE"
    )
    void commentReaction_sadface() {
        CommentReaction reaction = new CommentReaction(Reaction.SADFACE, "c1", "u2");
        assertEquals(Reaction.SADFACE, reaction.getReaction());
        assertEquals("u2", reaction.getUserId());
    }

    // ======================== Comment ========================

    @Test
    @CasoPrueba(
            id = "CP111",
            descripcion = "Comment - constructor vacío crea objeto sin lanzar excepción",
            entrada = "Sin parámetros",
            tipo = "Normal",
            esperado = "El objeto Comment no es null"
    )
    void comment_emptyConstructor() {
        Comment comment = new Comment();
        assertNotNull(comment);
    }

    @Test
    @CasoPrueba(
            id = "CP112",
            descripcion = "Comment - setters y getters de todos los campos básicos",
            entrada = "id=c-1, title=Nuevo titulo, category=backend, username=pepe, happyFace=3, sadFace=1",
            tipo = "Normal",
            esperado = "Todos los getters retornan los valores seteados correctamente"
    )
    void comment_settersAndGetters() {
        Comment comment = new Comment();
        comment.setId("c-1");
        comment.setTitle("Nuevo titulo");
        comment.setCategory("backend");
        comment.setUsername("pepe");
        comment.setHappyFace(3);
        comment.setSadFace(1);

        assertEquals("c-1", comment.getId());
        assertEquals("Nuevo titulo", comment.getTitle());
        assertEquals("backend", comment.getCategory());
        assertEquals("pepe", comment.getUsername());
        assertEquals(3, comment.getHappyFace());
        assertEquals(1, comment.getSadFace());
    }

    @Test
    @CasoPrueba(
            id = "CP113",
            descripcion = "Comment - detach elimina observer y notifyObservers no falla con lista vacía",
            entrada = "Observer adjuntado y luego removido",
            tipo = "Normal",
            esperado = "No lanza excepción al notificar con lista de observers vacía"
    )
    void comment_detachObserver() {
        Comment comment = new Comment("Titulo", "Contenido", "general", List.of(), "user1", false, 0, 0);
        User user = new User("A", "B", "ab", "ab@test.com", "pass", "123", Role.USER, 0);
        comment.attach(user);
        comment.detach(user);
        assertDoesNotThrow(() -> comment.notifyObservers("test message"));
    }

    @Test
    @CasoPrueba(
            id = "CP114",
            descripcion = "Comment - subscribe no duplica el username en subscribedUsernames",
            entrada = "username=pepe suscrito dos veces",
            tipo = "Logica Negocio",
            esperado = "subscribedUsernames contiene solo una entrada para 'pepe'"
    )
    void comment_subscribeNoDuplicates() {
        Comment comment = new Comment("T", "C", "cat", List.of(), "user1", false, 0, 0);
        comment.subscribe("pepe");
        comment.subscribe("pepe");
        assertEquals(1, comment.getSubscribedUsernames().size());
        assertTrue(comment.getSubscribedUsernames().contains("pepe"));
    }

    @Test
    @CasoPrueba(
            id = "CP115",
            descripcion = "Comment - toComponent retorna CommentLeaf cuando no tiene replies",
            entrada = "Comment sin replies",
            tipo = "Normal",
            esperado = "toComponent retorna objeto CommentComponent no nulo"
    )
    void comment_toComponentLeaf() {
        Comment comment = new Comment("T", "C", "cat", List.of(), "user1", false, 0, 0);
        comment.setId("c-leaf");
        CommentComponent component = comment.toComponent();
        assertNotNull(component);
    }

    @Test
    @CasoPrueba(
            id = "CP116",
            descripcion = "Comment - toComponent retorna CommentComposite cuando tiene replies",
            entrada = "Comment con una reply",
            tipo = "Normal",
            esperado = "toComponent retorna CommentComponent no nulo"
    )
    void comment_toComponentComposite() {
        Comment parent = new Comment("T", "C", "cat", List.of(), "user1", false, 0, 0);
        parent.setId("c-parent");
        Comment reply = new Comment("", "Reply", "cat", null, "user2", false, 0, 0);
        reply.setId("c-reply");
        parent.getReplies().add(reply);
        CommentComponent component = parent.toComponent();
        assertNotNull(component);
    }

    @Test
    @CasoPrueba(
            id = "CP117",
            descripcion = "Comment - setReactions y getReactions funcionan correctamente",
            entrada = "Lista con un CommentReaction HAPPYFACE",
            tipo = "Normal",
            esperado = "getReactions retorna la lista con 1 elemento"
    )
    void comment_setAndGetReactions() {
        Comment comment = new Comment("T", "C", "cat", List.of(), "user1", false, 0, 0);
        List<CommentReaction> reactions = new ArrayList<>();
        reactions.add(new CommentReaction(Reaction.HAPPYFACE, "c1", "u1"));
        comment.setReactions(reactions);
        assertEquals(1, comment.getReactions().size());
        assertEquals(Reaction.HAPPYFACE, comment.getReactions().get(0).getReaction());
    }

    @Test
    @CasoPrueba(
            id = "CP118",
            descripcion = "Comment - getReplies retorna lista vacía al crear un nuevo comentario",
            entrada = "Comment recién creado con constructor completo",
            tipo = "Normal",
            esperado = "getReplies retorna lista no nula y vacía"
    )
    void comment_getRepliesEmptyOnCreation() {
        Comment comment = new Comment("T", "C", "cat", List.of("tag"), "user1", false, 0, 0);
        assertNotNull(comment.getReplies());
        assertTrue(comment.getReplies().isEmpty());
    }

    @Test
    @CasoPrueba(
            id = "CP119",
            descripcion = "Comment - getContent retorna el contenido asignado en el constructor",
            entrada = "content=Contenido de prueba",
            tipo = "Normal",
            esperado = "getContent retorna 'Contenido de prueba'"
    )
    void comment_getContent() {
        Comment comment = new Comment("Titulo", "Contenido de prueba", "cat", List.of(), "user1", false, 0, 0);
        assertEquals("Contenido de prueba", comment.getContent());
    }
}
