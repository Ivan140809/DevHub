package com.skillstack.devhub.service;


import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.exception.CommentNotFoundException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.Comment;
import com.skillstack.devhub.model.CommentComponent;
import com.skillstack.devhub.model.CommentComposite;
import com.skillstack.devhub.model.User;
import com.skillstack.devhub.repository.CommentRepository;
import com.skillstack.devhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public CommentDTO createComment(String content, String username, boolean isStarred) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + username));

        Comment comment = new Comment(content, username, isStarred);

        user.setEmailSenderService(emailSenderService);
        comment.attach(user);
        comment.subscribe(username);

        commentRepository.save(comment);

        return comment.toComponent().toDTO();
    }

    public CommentDTO getCommentTree(String id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("COMENTARIO CON ID "+id+" NO ENCONTRADO"));
        CommentComponent commentTree = comment.toComponent();

        return commentTree.toDTO();
    }

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

        User replier = userRepository.findByUsername(replyUsername)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + replyUsername));

        replier.setEmailSenderService(emailSenderService);
        parent.attach(replier);
        parent.subscribe(replyUsername);

        Comment reply = new Comment(content, replyUsername, isStarred);
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
}
