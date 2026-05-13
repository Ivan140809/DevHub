package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.dto.ReactionDTO;
import com.skillstack.devhub.exception.CommentNotFoundException;
import com.skillstack.devhub.model.*;
import com.skillstack.devhub.repository.CommentRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class CommentServiceExtendedTest {

    @Mock private CommentRepository commentRepository;
    @Mock private UserRepository userRepository;
    @Mock private EmailSenderService emailSenderService;

    @InjectMocks
    private CommentService commentService;

    // ======================== addReaction ========================

    @Test
    @CasoPrueba(
            id = "CP500",
            descripcion = "addReaction - SADFACE incrementa sadFace en 1",
            entrada = "reaction=SADFACE, comentario con sadFace=0",
            tipo = "Normal",
            esperado = "sadFace=1, happyFace=0"
    )
    void addReaction_sadface_incrementsSadFace() {
        Comment root = new Comment("Titulo", "Contenido", "general", List.of("tag"), "user1", false, 0, 0);
        root.setId("root-1");
        ReactionDTO reactionDTO = new ReactionDTO(Reaction.SADFACE, "root-1", "user-abc");

        when(commentRepository.findById("root-1")).thenReturn(Optional.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        CommentDTO result = commentService.addReaction(reactionDTO);

        assertNotNull(result);
        assertEquals(0, result.getHappyFace());
        assertEquals(1, result.getSadFace());
        verify(commentRepository).save(root);
    }

    @Test
    @CasoPrueba(
            id = "CP501",
            descripcion = "addReaction - usuario cambia de SADFACE a HAPPYFACE ajusta ambos contadores",
            entrada = "usuario ya tiene SADFACE, envía HAPPYFACE",
            tipo = "Logica Negocio",
            esperado = "happyFace=1, sadFace=-1 (decremento desde 0+anterior)"
    )
    void addReaction_switchFromSadfaceToHappyface_adjustsCounters() {
        Comment root = new Comment("Titulo", "Contenido", "general", List.of(), "user1", false, 0, 1);
        root.setId("root-sad");
        List<CommentReaction> existing = new ArrayList<>();
        existing.add(new CommentReaction(Reaction.SADFACE, "root-sad", "u-switch"));
        root.setReactions(existing);

        ReactionDTO reactionDTO = new ReactionDTO(Reaction.HAPPYFACE, "root-sad", "u-switch");

        when(commentRepository.findById("root-sad")).thenReturn(Optional.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        CommentDTO result = commentService.addReaction(reactionDTO);

        assertNotNull(result);
        assertEquals(1, result.getHappyFace());
        assertEquals(0, result.getSadFace());
        verify(commentRepository).save(root);
    }

    @Test
    @CasoPrueba(
            id = "CP502",
            descripcion = "addReaction - usuario cambia de HAPPYFACE a SADFACE ajusta ambos contadores",
            entrada = "usuario ya tiene HAPPYFACE, envía SADFACE",
            tipo = "Logica Negocio",
            esperado = "sadFace=1, happyFace decrementado"
    )
    void addReaction_switchFromHappyfaceToSadface_adjustsCounters() {
        Comment root = new Comment("Titulo", "Contenido", "general", List.of(), "user1", false, 1, 0);
        root.setId("root-happy");
        List<CommentReaction> existing = new ArrayList<>();
        existing.add(new CommentReaction(Reaction.HAPPYFACE, "root-happy", "u-switch2"));
        root.setReactions(existing);

        ReactionDTO reactionDTO = new ReactionDTO(Reaction.SADFACE, "root-happy", "u-switch2");

        when(commentRepository.findById("root-happy")).thenReturn(Optional.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        CommentDTO result = commentService.addReaction(reactionDTO);

        assertNotNull(result);
        assertEquals(1, result.getSadFace());
        assertEquals(0, result.getHappyFace());
        verify(commentRepository).save(root);
    }

    @Test
    @CasoPrueba(
            id = "CP503",
            descripcion = "addReaction - misma reacción del mismo usuario no cambia los contadores",
            entrada = "usuario ya tiene HAPPYFACE, envía HAPPYFACE de nuevo",
            tipo = "Logica Negocio",
            esperado = "Los contadores no cambian, retorna el DTO sin guardar"
    )
    void addReaction_sameReactionFromSameUser_noChange() {
        Comment root = new Comment("Titulo", "Contenido", "general", List.of(), "user1", false, 1, 0);
        root.setId("root-same");
        List<CommentReaction> existing = new ArrayList<>();
        existing.add(new CommentReaction(Reaction.HAPPYFACE, "root-same", "u-same"));
        root.setReactions(existing);

        ReactionDTO reactionDTO = new ReactionDTO(Reaction.HAPPYFACE, "root-same", "u-same");

        when(commentRepository.findById("root-same")).thenReturn(Optional.of(root));

        CommentDTO result = commentService.addReaction(reactionDTO);

        assertNotNull(result);
        assertEquals(1, result.getHappyFace());
        verify(commentRepository, never()).save(any());
    }

    @Test
    @CasoPrueba(
            id = "CP504",
            descripcion = "addReaction - ID no encontrado en root ni en árbol lanza CommentNotFoundException",
            entrada = "commentId=noexiste",
            tipo = "Negativa",
            esperado = "CommentNotFoundException lanzada"
    )
    void addReaction_commentNotFound_throwsCommentNotFoundException() {
        when(commentRepository.findById("noexiste")).thenReturn(Optional.empty());
        when(commentRepository.findAll()).thenReturn(List.of());

        ReactionDTO reactionDTO = new ReactionDTO(Reaction.HAPPYFACE, "noexiste", "u1");

        assertThrows(CommentNotFoundException.class,
                () -> commentService.addReaction(reactionDTO));
    }

    // ======================== getStarredComments ========================

    @Test
    @CasoPrueba(
            id = "CP505",
            descripcion = "getStarredComments - lista vacía lanza CommentNotFoundException",
            entrada = "No hay comentarios con isStarred=true",
            tipo = "Negativa",
            esperado = "CommentNotFoundException lanzada"
    )
    void getStarredComments_empty_throwsCommentNotFoundException() {
        when(commentRepository.findByIsStarred(true)).thenReturn(List.of());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.getStarredComments());
    }

    // ======================== deleteComment ========================

    @Test
    @CasoPrueba(
            id = "CP506",
            descripcion = "deleteComment - ID no encontrado en ningún árbol lanza CommentNotFoundException",
            entrada = "commentId=totalmente-inexistente",
            tipo = "Negativa",
            esperado = "CommentNotFoundException lanzada"
    )
    void deleteComment_notFoundAnywhere_throwsCommentNotFoundException() {
        when(commentRepository.existsById("totalmente-inexistente")).thenReturn(false);
        when(commentRepository.findAll()).thenReturn(List.of());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.deleteComment("totalmente-inexistente"));
    }

    @Test
    @CasoPrueba(
            id = "CP507",
            descripcion = "deleteComment - reply anidada en segundo nivel es eliminada correctamente",
            entrada = "Árbol: root -> hijo -> nieto con ID a eliminar",
            tipo = "Normal",
            esperado = "El nieto es eliminado del árbol y root se guarda"
    )
    void deleteComment_deepNestedReply_removedSuccessfully() {
        Comment nieto = new Comment("", "Nieto", "cat", null, "user3", false, 0, 0);
        nieto.setId("nieto-1");

        Comment hijo = new Comment("", "Hijo", "cat", null, "user2", false, 0, 0);
        hijo.setId("hijo-1");
        hijo.getReplies().add(nieto);

        Comment root = new Comment("Raiz", "Contenido", "cat", List.of(), "user1", false, 0, 0);
        root.setId("root-deep");
        root.getReplies().add(hijo);

        when(commentRepository.existsById("nieto-1")).thenReturn(false);
        when(commentRepository.findAll()).thenReturn(List.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> commentService.deleteComment("nieto-1"));

        assertTrue(hijo.getReplies().isEmpty());
        verify(commentRepository).save(root);
    }

    // ======================== addReply ========================

    @Test
    @CasoPrueba(
            id = "CP508",
            descripcion = "addReply - padre no encontrado lanza CommentNotFoundException",
            entrada = "parentId=noexiste",
            tipo = "Negativa",
            esperado = "CommentNotFoundException lanzada"
    )
    void addReply_parentNotFound_throwsCommentNotFoundException() {
        when(commentRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.addReply("noexiste", "Contenido", "user@test.com", false));
    }

    // ======================== editComment ========================

    @Test
    @CasoPrueba(
            id = "CP509",
            descripcion = "editComment - comentario no encontrado en ningún árbol lanza CommentNotFoundException",
            entrada = "commentId=noexiste",
            tipo = "Negativa",
            esperado = "CommentNotFoundException lanzada"
    )
    void editComment_notFoundAnywhere_throwsCommentNotFoundException() {
        when(commentRepository.findById("noexiste")).thenReturn(Optional.empty());
        when(commentRepository.findAll()).thenReturn(List.of());

        assertThrows(CommentNotFoundException.class,
                () -> commentService.editComment("noexiste", "Nuevo contenido"));
    }

    @Test
    @CasoPrueba(
            id = "CP510",
            descripcion = "createComment - usuario encontrado por username (no email) crea comentario correctamente",
            entrada = "identifier=ivansitol (username), no email match",
            tipo = "Normal",
            esperado = "Se crea el comentario y retorna CommentDTO no nulo"
    )
    void createComment_userFoundByUsername_createsComment() {
        User user = new User("Ivan", "L", "ivansitol", "ivan@test.com", "pass", "123", Role.USER, 0);
        when(userRepository.findByEmail("ivansitol")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("ivansitol")).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId("c-username");
            return c;
        });

        CommentDTO result = commentService.createComment(
                "Titulo", "Contenido", "general", List.of(), "ivansitol", false, 0, 0
        );

        assertNotNull(result);
        assertEquals("ivansitol", result.getUsername());
        verify(commentRepository).save(any(Comment.class));
    }
}
