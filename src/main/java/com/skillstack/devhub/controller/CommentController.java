package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable String commentId) {
        CommentDTO comment = commentService.getCommentTree(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comment);
    }

}
