package com.skillstack.devhub.service;


import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.exception.CommentNotFoundException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.*;
import com.skillstack.devhub.repository.CommentRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, EmailSenderService emailSenderService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
    }

    // esto sirve para reconectar observers desde BD
    private void reattachObservers(Comment comment) {
        for (String username : comment.getSubscribedUsernames()) {
            userRepository.findByUsername(username)
                    .ifPresent(user -> {
                        user.setEmailSenderService(emailSenderService);
                        comment.attach(user);
                    });
        }
    }

    public CommentDTO createComment(String title, String content, String category, List<String> tags, String userEmail, boolean isStarred, int happyFace, int sadFace) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        User user = userRepository.findByEmail(userEmail)
                .or(() -> userRepository.findByUsername(userEmail))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + userEmail));

        String authorUsername = user.getUsername() != null ? user.getUsername() : userEmail;
        Comment comment = new Comment(title, content, category, tags, authorUsername, isStarred, happyFace, sadFace);

        user.setEmailSenderService(emailSenderService);
        comment.attach(user);
        comment.subscribe(authorUsername);

        commentRepository.save(comment);

        return comment.toComponent().toDTO();
    }

    public CommentDTO getCommentTree(String id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("COMENTARIO CON ID "+id+" NO ENCONTRADO"));
        CommentComponent commentTree = comment.toComponent();

        return commentTree.toDTO();
    }

    // Permite editar el contenido de un comentario existente y notifica a observadores
    public CommentDTO editComment(String commentId, String newContent) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("COMENTARIO CON ID " + commentId + " NO ENCONTRADO"));

        reattachObservers(comment);
        comment.setContent(newContent);
        commentRepository.save(comment);

        return comment.toComponent().toDTO();
    }

    public CommentDTO addReply(String parentId, String content, String replyUsername, boolean isStarred) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentNotFoundException("COMENTARIO CON ID " + parentId + " NO ENCONTRADO"));

        reattachObservers(parent);

        User replier = userRepository.findByEmail(replyUsername)
                .or(() -> userRepository.findByUsername(replyUsername))
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + replyUsername));

        replier.setEmailSenderService(emailSenderService);
        parent.attach(replier);
        parent.subscribe(replier.getUsername());

        Comment reply = new Comment("", content, parent.getCategory(), null, replier.getUsername(), isStarred, 0, 0);
        parent.addReply(reply);
        commentRepository.save(parent);

        return parent.toComponent().toDTO();
    }

    public List<CommentDTO> getStarredComments(){

        List<Comment> starred = commentRepository.findByIsStarred(true);

        if(starred.isEmpty()){
            throw new CommentNotFoundException("No se ha encontrado ningun comentario destacado");
        }

        return starred.stream().map(c -> c.toComponent().toDTO()).toList();
    }

    public CommentDTO addReaction (String commentId, Reaction reaction){
        Comment rootComment = findRootCommentById(commentId);
        Comment targetComment = findCommentInTree(rootComment, commentId);
        if (targetComment == null) {
            throw new RuntimeException("Comentario no encontrado");
        }

        if (reaction.equals(Reaction.HAPPYFACE)){
            targetComment.setHappyFace(targetComment.getHappyFace()+1);
        } else {
            targetComment.setSadFace(targetComment.getSadFace()+1);
        }

        commentRepository.save(rootComment);
        return targetComment.toComponent().toDTO();
    }

    private Comment findRootCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .orElseGet(() -> {
                    for (Comment root : commentRepository.findAll()) {
                        if (findCommentInTree(root, commentId) != null) {
                            return root;
                        }
                    }
                    throw new RuntimeException("Comentario no encontrado");
                });
    }

    private Comment findCommentInTree(Comment comment, String commentId) {
        if (comment.getId().equals(commentId)) {
            return comment;
        }
        if (comment.getReplies() == null) {
            return null;
        }
        for (Comment reply : comment.getReplies()) {
            Comment found = findCommentInTree(reply, commentId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public List<CommentDTO> getCommentsMostReactions(){

        List<Comment> commentList= commentRepository.findTop7ByOrderByHappyFaceDesc();

        List<CommentComponent> commentComponents = new ArrayList<>();
        for(Comment c : commentList){
            commentComponents.add(c.toComponent());
        }
        List<CommentDTO> commentDTOS = new ArrayList<>();

        for(CommentComponent c : commentComponents){
            commentDTOS.add(c.toDTO());
        }
        return commentDTOS;
    }
}
