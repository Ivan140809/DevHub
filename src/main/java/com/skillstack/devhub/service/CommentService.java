package com.skillstack.devhub.service;


import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.exception.CommentNotFoundException;
import com.skillstack.devhub.exception.UserNotFoundException;
import com.skillstack.devhub.model.Comment;
import com.skillstack.devhub.model.CommentComponent;
import com.skillstack.devhub.model.CommentComposite;
import com.skillstack.devhub.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentDTO getCommentTree(String id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("COMENTARIO CON ID "+id+" NO ENCONTRADO"));
        CommentComponent commentTree = comment.toComponent();

        return commentTree.toDTO();
    }

}
