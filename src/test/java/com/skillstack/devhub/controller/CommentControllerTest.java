package com.skillstack.devhub.controller;

import com.skillstack.devhub.CasoPrueba;
import com.skillstack.devhub.CasoPruebaExtension;
import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.exception.CommentNotFoundException;
import com.skillstack.devhub.dto.ReactionDTO;
import com.skillstack.devhub.model.Reaction;
import com.skillstack.devhub.dto.CreateCommentRequest;
import com.skillstack.devhub.dto.CreateReplyRequest;
import com.skillstack.devhub.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, CasoPruebaExtension.class})
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentController commentController;


    // CP01 - Normal: Crear una discusion con datos válidos :)

    @Test
    @CasoPrueba(
            id = "CP01",
            descripcion= "Crear discusion con datos validos",
            entrada= "email=ana@devhub.com, titulo=Discusion, contenido=prueba, categoria=general",
            tipo = "Normal",
            esperado = "Se crea una discución"
    )
    void createComment() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setTitle("Discusion");
        request.setContent("prueba");
        request.setCategory("general");

        CommentDTO expectedDTO = new CommentDTO(null, "Discusion", "prueba", "general", null, null, null, false, null, 0, 0);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("ana@devhub.com");
        when(commentService.createComment(
                eq("Discusion"), eq("prueba"), eq("general"),
                eq(null), eq("ana@devhub.com"),
                eq(false), eq(0), eq(0)
        )).thenReturn(expectedDTO);

        ResponseEntity<CommentDTO> response = commentController.createComment(request, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Discusion", response.getBody().getTitle());

        System.out.println("CP01 Discusion creada correctamente:");
        System.out.println("Titulo: " + response.getBody().getTitle());
        System.out.println("Contenido: "+response.getBody().getContent());
        System.out.println("Status: " + response.getStatusCode());

        verify(commentService).createComment(
                eq("Discusion"), eq("prueba"), eq("general"),
                eq(null), eq("ana@devhub.com"),
                eq(false), eq(0), eq(0)
        );
    }

    // CP02 - Negativa: Comentario con ID inexistente lanza CommentNotFoundException
    @Test
    @CasoPrueba(
            id= "CP02",
            descripcion = "Comentario con ID inexistente lanza CommentNotFoundException",
            entrada = "commentId=0",
            tipo= "Negativa",
            esperado ="CommentNotFoundException"
    )
    void getCommentWhenIdNotFound() {
        when(commentService.getCommentTree("0"))
                .thenThrow(new CommentNotFoundException("Comentario no encontrado"));
        Exception ex =assertThrows(CommentNotFoundException.class,
                () -> commentController.getComment("0"));

        System.out.println("CP02 Excepcion:");
        System.out.println("Mensaje: " +ex.getMessage());

        verify(commentService).getCommentTree("0");
    }

    // CP03 - Normal: Agregar respuesta a una discusion existente
    @Test
    @CasoPrueba(
            id = "CP03",
            descripcion = "Agregar respuesta a una discusion existente",
            entrada= "commentId=1, contenido=trump, usuario=luisdiaz@devhub.com",
            tipo = "Normal",
            esperado= "Retorna 201 CREATED con el CommentDTO del padre actualizado"
    )
    void addReply() {
        CreateReplyRequest request = new CreateReplyRequest();
        request.setContent("trump");

        CommentDTO expectedDTO = new CommentDTO("1", "trump o biden", null, null, null, null, null, false, null, 0, 0);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("luisdiaz@devhub.com");
        when(commentService.addReply(
                eq("1"),
                eq("trump"),
                eq("luisdiaz@devhub.com"),
                eq(false)
        )).thenReturn(expectedDTO);

        ResponseEntity<CommentDTO> response = commentController.addReply("1", request, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());

        System.out.println("Status: " + response.getStatusCode());

        verify(commentService).addReply(
                eq("1"),
                eq("trump"),
                eq("luisdiaz@devhub.com"),
                eq(false)
        );
    }


    // CP04 - Normal: Agregar reaccion HAPPYFACE
    @Test
    @CasoPrueba(
            id = "CP04",
            descripcion = "Agregar reaccion HAPPYFACE",
            entrada = "1, reaction=HAPPYFACE, usuario=james@devhub.com",
            tipo= "Normal",
            esperado = "CommentDTO actualizado con happyFace=1"
    )
    void addReactionHAPPYFACE() {
        CommentDTO expectedDTO = new CommentDTO(null, null, null, null, null, null, null, false, null, 1, 0);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("james@devhub.com");
        when(commentService.addReaction(any(ReactionDTO.class))).thenReturn(expectedDTO);

        ResponseEntity<CommentDTO> response = commentController.addReaction(
                "1", Reaction.HAPPYFACE, authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getHappyFace());
        assertEquals(0, response.getBody().getSadFace());

        System.out.println("CP04 Reacción HAPPYFACE agregada:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("HappyFace: " + response.getBody().getHappyFace());
        System.out.println("SadFace: " + response.getBody().getSadFace());

        verify(commentService).addReaction(any(ReactionDTO.class));
    }

    // CP05 Normal: Obtener comentarios destacados

    @Test
    @CasoPrueba(
            id          = "CP05",
            descripcion = "Obtener comentarios destacados",
            entrada     = "Dos comentarios starred=true, ids= 1, 2",
            tipo        = "Normal",
            esperado    = "Retorna la lista de CommentDTO con starred=true"
    )
    void getStarredComments() {
        CommentDTO starred1 = new CommentDTO("1", null, null, null, null, null, null, true, null, 0, 0);
        CommentDTO starred2 = new CommentDTO("2", null, null, null, null, null, null, true, null, 0, 0);

        when(commentService.getStarredComments()).thenReturn(List.of(starred1, starred2));

        ResponseEntity<List<CommentDTO>> response = commentController.getStarredComments();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().get(0).isStarred());
        assertTrue(response.getBody().get(1).isStarred());

        System.out.println("CP05 Comentarios destacados obtenidos correctamente:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Cantidad de comentarios destacados: " +response.getBody().size());

        verify(commentService).getStarredComments();
    }

    //CP06 - Normal: Obtener top comentarios por HAPPYFACE
    @Test
    @CasoPrueba(
            id = "CP06",
            descripcion = "Obtener top comentarios por HAPPYFACE",
            entrada= "Dos comentarios, happyFace = 10, 5",
            tipo= "Normal",
            esperado= "Retorna la lista de CommentDTO ordenada por reacciones"
    )
    void getCommentsWithMostReactions() {
        CommentDTO top1 = new CommentDTO("1", null, null, null, null, null, null, true, null, 10, 0);
        CommentDTO top2 = new CommentDTO("2", null, null, null, null, null, null, true, null, 5, 0);

        when(commentService.getCommentsMostReactions()).thenReturn(List.of(top1, top2));

        ResponseEntity<List<CommentDTO>> response = commentController.getCommentsMostReactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("1", response.getBody().get(0).getId());
        assertEquals(10, response.getBody().get(0).getHappyFace());

        System.out.println("CP06 Top comentarios:");
        System.out.println("Status  : " + response.getStatusCode());
        for (int i = 0; i < response.getBody().size(); i++) {
            System.out.println((i + 1)+"." + " ID: " + response.getBody().get(i).getId()
                    + " Cantidad reacciones: " + response.getBody().get(i).getHappyFace());
        }
        verify(commentService).getCommentsMostReactions();
    }

    //CP07 Normal: Editar comentario existente

    @Test
    @CasoPrueba(
            id = "CP07",
            descripcion = "Editar contenido de comentario existente",
            entrada= "commentId=1, newContent=me esclavizaron para hacer estas pruebas ayuda D:",
            tipo = "Normal",
            esperado= "CommentDTO con el contenido actualizado"
    )
    void editComment() {
        CommentDTO expectedDTO = new CommentDTO("1", null, "me esclavizaron para hacer estas pruebas ayuda D:", null, null, null, null, true, null, 0, 0);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("ivan@devhub.com");
        when(commentService.editComment("1", "me esclavizaron para hacer estas pruebas ayuda D:"))
                .thenReturn(expectedDTO);

        ResponseEntity<CommentDTO> response = commentController.editComment(
                "1", "me esclavizaron para hacer estas pruebas ayuda D:", authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("me esclavizaron para hacer estas pruebas ayuda D:", response.getBody().getContent());

        System.out.println("CP07 Comentario editado correctamente:");
        System.out.println("StatuS: " + response.getStatusCode());
        System.out.println("Nuevo contenido: " +response.getBody().getContent());

        verify(commentService).editComment("1", "me esclavizaron para hacer estas pruebas ayuda D:");
    }

    // CP08 - Normal: Eliminar comentario
    @Test
    @CasoPrueba(
            id = "CP08",
            descripcion = "Eliminar comentario existente",
            entrada= "commentId=1",
            tipo = "Normal",
            esperado = "Retorna 204 NO_CONTENT"
    )
    void deleteComment() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("megustaelazul@devhub.com");

        ResponseEntity<Void> response = commentController.deleteComment("1", authentication);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        System.out.println("CP08 Comentario eliminado correctamente:");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Contenido: " + response.getBody());

        verify(commentService).deleteComment("1");
    }
}
