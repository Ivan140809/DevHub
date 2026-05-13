package com.skillstack.devhub.service;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.dto.ReactionDTO;
import com.skillstack.devhub.exception.CommentNotFoundException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.Comment;
import com.skillstack.devhub.model.Reaction;
import com.skillstack.devhub.model.Role;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.CommentRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class CommentTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private CommentService commentService;


    // CP21 - Normal: crear discusion en el foro con datos validos retorna CommentDTO no nulo

    @Test
    @CasoPrueba(
        id          = "CP21",
        descripcion = "Crear discusion en el foro",
        entrada     = "Titulo=Nueva discusion, contenido=Contenido de prueba, categoria=general, usuario=ivan@example.com",
        tipo        = "Normal",
        esperado    = "Se crea la discusion, se guarda en el repositorio y retorna un CommentDTO no nulo"
    )


    void newComment_emailFound() {
        User user = new User("Ana", "Murcia", "anamurcia", "ana@devhub.com", "password", "195000", Role.USER, 0);
        when(userRepository.findByEmail("ana@devhub.com")).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {

                Comment saved = invocation.getArgument(0);

                saved.setId("comment-1");

                 return saved;
             });
        CommentDTO result = commentService.createComment(
                "Nueva discusion",
                "Contenido de prueba",
                "general",
                List.of("test", "spring"),
                "ana@devhub.com",
                true, 0, 0
        );
        assertNotNull(result);

        assertEquals("Nueva discusion", result.getTitle());
        assertEquals("Contenido de prueba", result.getContent());
        assertEquals("general", result.getCategory());
        assertEquals("anamurcia", result.getUsername());
        assertEquals("comment-1", result.getId());
        assertTrue(result.isStarred());
        assertEquals(0, result.getHappyFace());
        assertEquals(0, result.getSadFace());
        assertTrue(result.getReplies().isEmpty());


        System.out.println("[CP21] Discusion creada correctamente al encontrar email:");
        System.out.println("  -> ID     : " + result.getId());
        System.out.println("  -> Titulo : " + result.getTitle());
        System.out.println("  -> Usuario: " + result.getUsername());

        verify(userRepository).findByEmail("ana@devhub.com");
        verify(commentRepository).save(any(Comment.class));
    }

    // CP22 - Normal: crear comentario a una discusion agrega la respuesta al padre y retorna CommentDTO
    @Test
    @CasoPrueba(
        id          = "CP22",
        descripcion = "Crear un comentario a una discusion",
        entrada     = "ID padre valido, contenido=Respuesta de prueba, usuario=lux@example.com",
        tipo        = "Normal",
        esperado    = "Se agrega la respuesta al padre, se guarda y retorna un CommentDTO"
    )
    void addReply_whenParentExists() {
        Comment parent = new Comment("Titulo principal", "Contenido padre", "general", List.of("tag"), "parentuser", false, 0, 0);
        parent.setId("parent-1");

        User replier = new User("lucas", "fuentes", "lux", "lux@example.com", "password", "123456", Role.USER, 0);
        when(commentRepository.findById("parent-1")).thenReturn(Optional.of(parent));
        when(userRepository.findByEmail("lux@example.com")).thenReturn(Optional.of(replier));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO result = commentService.addReply("parent-1", "Respuesta de prueba", "lux@example.com", false);

        assertNotNull(result);
        assertEquals("parent-1", result.getId());
        assertEquals(1, result.getReplies().size());
        assertEquals("Respuesta de prueba", result.getReplies().get(0).getContent());

        System.out.println("[CP22] Respuesta agregada correctamente:");
        System.out.println("  -> ID padre     : " + result.getId());
        System.out.println("  -> Total replies: " + result.getReplies().size());
        System.out.println("  -> Contenido    : " + result.getReplies().get(0).getContent());

        verify(commentRepository).save(parent);
        verify(emailSenderService).sendEmail(eq("lux@example.com"), anyString(), anyString());
    }

    // CP23 - Normal: darle like (HAPPYFACE) a una discusion incrementa en 1 el contador
    @Test
    @CasoPrueba(
        id          = "CP23",
        descripcion = "Darle like a una discusion",
        entrada     = "ID comentario valido, reaccion: HAPPYFACE",
        tipo        = "Normal",
        esperado    = "Se incrementa en 1 el contador de happyFace"
    )
    void addReaction_whenRootCommentFound() {
        Comment root = new Comment("Titulo", "Contenido", "general", List.of("tag"), "ivansitol", false, 0, 0);
        root.setId("root-1");
        ReactionDTO reactionDTO = new ReactionDTO(Reaction.HAPPYFACE, "root-1", "123");
        when(commentRepository.findById("root-1")).thenReturn(Optional.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO result = commentService.addReaction(reactionDTO);

        assertNotNull(result);
        assertEquals(1, result.getHappyFace());
        assertEquals(0, result.getSadFace());

        System.out.println("[CP23] Reaccion registrada correctamente:");
        System.out.println("  -> HappyFace: " + result.getHappyFace() + " (esperado: 1)");
        System.out.println("  -> SadFace  : " + result.getSadFace()   + " (esperado: 0)");

        verify(commentRepository).save(root);
    }

    // CP24 - Normal: obtener discusiones con mas likes retorna lista ordenada por happyFace descendente
    @Test
    @CasoPrueba(
        id          = "CP24",
        descripcion = "Obtener discusiones con mas likes",
        entrada     = "Lista retornada por findTop7ByOrderByHappyFaceDesc()",
        tipo        = "Normal",
        esperado    = "Retorna lista de comentarios ordenados por mayor cantidad de likes"
    )
    void getCommentsMostReactions() {
        Comment top = new Comment("Mejor", "Contenido mejor", "general", List.of("tag"), "topuser", false, 5, 0);
        top.setId("top-1");
        Comment second = new Comment("Segundo", "Contenido segundo", "general", List.of("tag"), "seconduser", false, 3, 0);
        second.setId("top-2");
        when(commentRepository.findTop7ByOrderByHappyFaceDesc()).thenReturn(List.of(top, second));

        List<CommentDTO> result = commentService.getCommentsMostReactions();

        assertEquals(2, result.size());
        assertEquals("top-1", result.get(0).getId());
        assertEquals(5, result.get(0).getHappyFace());
        assertEquals("top-2", result.get(1).getId());
        assertEquals(3, result.get(1).getHappyFace());

        System.out.println("[CP24] Lista ordenada por likes:");
        for (int i = 0; i < result.size(); i++) {
            System.out.println("  -> [" + (i + 1) + "] " + result.get(i).getTitle()
                    + " | HappyFace: " + result.get(i).getHappyFace());
        }
    }

    // CP25 - Normal: editar comentario existente actualiza el contenido y lo guarda nuevamente
    @Test
    @CasoPrueba(
        id          = "CP25",
        descripcion = "Editar el comentario existente",
        entrada     = "ID valido, nuevo contenido=Contenido editado",
        tipo        = "Normal",
        esperado    = "El contenido del comentario cambia y se guarda nuevamente"
    )
    void editComment_whenCommentFound() {
        Comment comment = new Comment("Titulo", "Contenido original", "general", List.of("tag"), "ivansitol", false, 0, 0);
        comment.setId("comment-3");
        comment.subscribe("richi");

        User subscriber = new User("Richar", "Castillo", "richi", "richia@example.com", "password", "123456", Role.USER, 0);
        when(commentRepository.findById("comment-3")).thenReturn(Optional.of(comment));
        when(userRepository.findByUsername("richi")).thenReturn(Optional.of(subscriber));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO result = commentService.editComment("comment-3", "Contenido editado");

        assertNotNull(result);
        assertEquals("Contenido editado", result.getContent());

        System.out.println("[CP25] Comentario editado correctamente:");
        System.out.println("  -> Nuevo contenido: " + result.getContent());

        verify(commentRepository).save(comment);
        verify(emailSenderService).sendEmail(eq("richia@example.com"), anyString(), contains("Hola richi"));
        System.out.println("  -> Notificacion enviada a: richia@example.com");
    }

    // CP26 - Negativa: titulo vacio o en blanco debe ser rechazado
    @Test
    @CasoPrueba(
        id          = "CP26",
        descripcion = "Crear discusion con titulo vacio",
        entrada     = "Titulo=\" \", contenido valido, usuario valido",
        tipo        = "Negativa",
        esperado    = "El sistema rechaza la creacion y lanza IllegalArgumentException"
    )
    void createComment_whenTitleIsBlank_throwsIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> commentService.createComment(
                " ", "Contenido valido", "general", List.of("tag"), "ivan@example.com", false, 0, 0
        ));

        System.out.println("[CP26] Excepcion lanzada correctamente:");
        System.out.println("  -> Tipo   : " + ex.getClass().getSimpleName());
        System.out.println("  -> Mensaje: " + ex.getMessage());

        verify(userRepository, never()).findByEmail(anyString());
        verify(commentRepository, never()).save(any(Comment.class));
        System.out.println("  -> repositorio.save() NO fue llamado (correcto)");
    }

    // CP27 - Negativa: usuario inexistente lanza UserNotFoundException
    @Test
    @CasoPrueba(
        id          = "CP27",
        descripcion = "Crear comentario sin haber iniciado sesion",
        entrada     = "Usuario inexistente: noexiste@example.com",
        tipo        = "Negativa",
        esperado    = "Lanza UserNotFoundException"
    )
    void createComment_whenUserNotFound_throwsUserNotFoundException() {
        when(userRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("noexiste@example.com")).thenReturn(Optional.empty());

        Exception ex = assertThrows(UserNotFoundException.class, () -> commentService.createComment(
                "Titulo valido", "Contenido valido", "general", List.of("tag"), "noexiste@example.com", false, 0, 0
        ));

        System.out.println("[CP27] Excepcion lanzada correctamente:");
        System.out.println("  -> Tipo   : " + ex.getClass().getSimpleName());
        System.out.println("  -> Mensaje: " + ex.getMessage());

        verify(commentRepository, never()).save(any(Comment.class));
        System.out.println("  -> repositorio.save() NO fue llamado (correcto)");
    }

    // CP28 - Borde: respuesta con contenido de 1 caracter se crea correctamente
    @Test
    @CasoPrueba(
        id          = "CP28",
        descripcion = "Crear respuesta con contenido de 1 caracter",
        entrada     = "Contenido=a, usuario valido, ID padre valido",
        tipo        = "Borde",
        esperado    = "Se crea la respuesta con el contenido minimo permitido"
    )
    void addReply_withOneCharContent_createsReplySuccessfully() {
        Comment parent = new Comment("Titulo principal", "Contenido padre", "general", List.of("tag"), "parentuser", false, 0, 0);
        parent.setId("parent-border");

        User replier = new User("Maria", "Garcia", "mgarcia", "mgarcia@example.com", "password", "123456", Role.USER, 0);
        when(commentRepository.findById("parent-border")).thenReturn(Optional.of(parent));
        when(userRepository.findByEmail("mgarcia@example.com")).thenReturn(Optional.of(replier));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO result = commentService.addReply("parent-border", "a", "mgarcia@example.com", false);

        assertNotNull(result);
        assertEquals(1, result.getReplies().size());
        assertEquals("a", result.getReplies().get(0).getContent());

        System.out.println("[CP28] Respuesta con 1 caracter creada correctamente:");
        System.out.println("  -> Contenido : \"" + result.getReplies().get(0).getContent() + "\"");
        System.out.println("  -> Longitud  : " + result.getReplies().get(0).getContent().length());

        verify(commentRepository).save(parent);
    }

    // CP29 - Borde: ID con formato invalido lanza CommentNotFoundException
    @Test
    @CasoPrueba(
        id          = "CP29",
        descripcion = "Obtener comentario con ID de formato incorrecto",
        entrada     = "ID: id_invalido",
        tipo        = "Borde",
        esperado    = "Lanza CommentNotFoundException"
    )
    void getCommentTree_withInvalidFormatId_throwsCommentNotFoundException() {
        when(commentRepository.findById("id_invalido")).thenReturn(Optional.empty());

        Exception ex = assertThrows(CommentNotFoundException.class, () ->
                commentService.getCommentTree("id_invalido"));

        System.out.println("[CP29] Excepcion lanzada correctamente:");
        System.out.println("  -> Tipo   : " + ex.getClass().getSimpleName());
        System.out.println("  -> Mensaje: " + ex.getMessage());
    }

    // CP30 - Logica de negocio: el creador queda suscrito automaticamente al crear la discusion
    @Test
    @CasoPrueba(
        id          = "CP30",
        descripcion = "El creador queda suscrito automaticamente",
        entrada     = "Usuario valido que crea comentario",
        tipo        = "Logica de Negocio",
        esperado    = "El username del creador queda dentro de subscribedUsernames"
    )
    void createComment_creatorIsAutoSubscribed() {
        User user = new User("Ivan", "Lastra", "ivansitol", "ivan@example.com", "password", "123456", Role.USER, 0);
        when(userRepository.findByEmail("ivan@example.com")).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            saved.setId("comment-sub");
            System.out.println("[CP30] Suscriptores al guardar: " + saved.getSubscribedUsernames());
            return saved;
        });

        commentService.createComment(
                "Titulo suscripcion", "Contenido", "general", List.of("tag"),
                "ivan@example.com", false, 0, 0
        );

        verify(commentRepository).save(argThat(comment -> {
            boolean suscrito = comment.getSubscribedUsernames().contains("ivansitol");
            System.out.println("[CP30] ivansitol en subscribedUsernames: " + suscrito + " (esperado: true)");
            return suscrito;
        }));
    }

    // CP31 - Normal: obtener comentarios destacados retorna lista de CommentDTO
    @Test
    @CasoPrueba(
            id          = "CP31",
            descripcion = "Obtener comentarios destacados (starred)",
            entrada     = "Dos comentarios con isStarred=true en el repositorio",
            tipo        = "Normal",
            esperado    = "Retorna lista de 2 CommentDTO con starred=true"
    )
    void getStarredCommentsWhenExistReturnslist() {
        Comment starred1 = new Comment("Destacado 1", "Contenido 1", "general", List.of("tag"), "user1", true, 0, 0);
        starred1.setId("star-1");
        Comment starred2 = new Comment("Destacado 2", "Contenido 2", "general", List.of("tag"), "user2", true, 0, 0);
        starred2.setId("star-2");

        when(commentRepository.findByIsStarred(true)).thenReturn(List.of(starred1, starred2));

        List<CommentDTO> result = commentService.getStarredComments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isStarred());
        assertTrue(result.get(1).isStarred());

        System.out.println("[CP31] Comentarios destacados obtenidos correctamente:");
        System.out.println("  -> Cantidad: " + result.size() + " (esperado: 2)");
        System.out.println("  -> [1] ID: " + result.get(0).getId() + " | Starred: " + result.get(0).isStarred());
        System.out.println("  -> [2] ID: " + result.get(1).getId() + " | Starred: " + result.get(1).isStarred());

        verify(commentRepository).findByIsStarred(true);
    }

    // CP32 - Normal: eliminar comentario raiz existente lo elimina del repositorio
    @Test
    @CasoPrueba(
            id          = "CP32",
            descripcion = "Eliminar comentario raiz existente",
            entrada     = "ID=1 existente como comentario raiz",
            tipo        = "Normal",
            esperado    = "Se elimina el comentario del repositorio y no lanza excepcion"
    )
    void deleteCommentWhenRootCommentExistsDeletesIt() {
        when(commentRepository.existsById("1")).thenReturn(true);

        assertDoesNotThrow(() -> commentService.deleteComment("1"));

        System.out.println("[CP32] Comentario raiz eliminado correctamente:");
        System.out.println("  -> ID: 1");

        verify(commentRepository).existsById("1");
        verify(commentRepository).deleteById("1");
    }

    // CP33 - Normal: findCommentInTree encuentra comentario anidado dentro del arbol
    @Test
    @CasoPrueba(
            id          = "CP33",
            descripcion = "Editar comentario anidado dentro del arbol",
            entrada     = "Comentario raiz con una reply anidada con ID=1",
            tipo        = "Normal",
            esperado    = "Se encuentra y edita el comentario anidado correctamente"
    )
    void findCommentInTreeWhenNestedCommentExistsEditsIt() {
        Comment reply = new Comment("", "Contenido reply", "general", null, "replyuser", false, 0, 0);
        reply.setId("1");

        Comment root = new Comment("Titulo raiz", "Contenido raiz", "general", List.of("tag"), "rootuser", false, 0, 0);
        root.setId("root-1");
        root.addReply(reply);

        when(commentRepository.findById("1")).thenReturn(Optional.empty());
        when(commentRepository.findAll()).thenReturn(List.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO result = commentService.editComment("1", "Contenido editado");

        assertNotNull(result);
        assertEquals("Contenido editado", result.getContent());

        System.out.println("[CP33] Comentario anidado encontrado y editado correctamente:");
        System.out.println("  -> ID reply     : 1");
        System.out.println("  -> Nuevo contenido: " + result.getContent());

        verify(commentRepository).save(root);
    }

    // CP34 - Normal: removeReplyFromTree elimina reply anidada y guarda el padre
    @Test
    @CasoPrueba(
            id          = "CP34",
            descripcion = "Eliminar reply anidada dentro del arbol de comentarios",
            entrada     = "Comentario raiz con una reply con ID=1",
            tipo        = "Normal",
            esperado    = "La reply es eliminada del arbol y el padre se guarda actualizado"
    )
    void removeReplyFromTreeWhenReplyExistsDeletesIt() {
        Comment reply = new Comment("", "Contenido reply", "general", null, "replyuser", false, 0, 0);
        reply.setId("1");

        Comment root = new Comment("Titulo raiz", "Contenido raiz", "general", List.of("tag"), "rootuser", false, 0, 0);
        root.setId("root-1");
        root.addReply(reply);

        when(commentRepository.existsById("1")).thenReturn(false);
        when(commentRepository.findAll()).thenReturn(List.of(root));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> commentService.deleteComment("1"));

        assertTrue(root.getReplies().isEmpty());

        System.out.println("[CP34] Reply eliminada del arbol correctamente:");
        System.out.println("  -> ID reply eliminada: 1");
        System.out.println("  -> Replies restantes : " + root.getReplies().size() + " (esperado: 0)");

        verify(commentRepository).save(root);
    }
}
