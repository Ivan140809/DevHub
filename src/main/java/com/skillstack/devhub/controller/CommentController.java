package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.service.CommentService;
import com.skillstack.devhub.model.Reaction;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{commentId:[0-9a-f]{24}}/reactions")
    public ResponseEntity<CommentDTO> addReaction(
            @PathVariable String commentId, @RequestParam Reaction reaction, Authentication authentication) {
 
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
 
        CommentDTO updated = commentService.addReaction(commentId, reaction);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updated);
    }

    @GetMapping("/starred")
    public ResponseEntity <List<CommentDTO>> getStarredComments(){
        List<CommentDTO> starred = commentService.getStarredComments();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(starred);
    }

    @GetMapping("/top")
    public ResponseEntity<List<CommentDTO>> getCommentsMostReactions() {
        List<CommentDTO> topComments = commentService.getCommentsMostReactions();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(topComments);
    }
}
