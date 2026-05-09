package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.dto.CreateCommentRequest;
import com.skillstack.devhub.dto.CreateReplyRequest;
import com.skillstack.devhub.dto.ReactionDTO;
import com.skillstack.devhub.model.Reaction;
import com.skillstack.devhub.service.CommentService;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestBody CreateCommentRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || ANONYMOUS_USER.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = authentication.getName();
        CommentDTO created = commentService.createComment(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                request.getTags(),
                userEmail,
                false,
                0,
                0);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable String commentId) {
        CommentDTO comment = commentService.getCommentTree(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comment);
    }

    @PostMapping("/{commentId:[0-9a-f]{24}}/replies")
    public ResponseEntity<CommentDTO> addReply(
            @PathVariable String commentId,
            @RequestBody CreateReplyRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || ANONYMOUS_USER.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = authentication.getName();
        CommentDTO updated = commentService.addReply(commentId, request.getContent(), userEmail, false);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @PostMapping("/{commentId:[0-9a-f]{24}}/reactions")
    public ResponseEntity<CommentDTO> addReaction(
            @PathVariable String commentId,
            @RequestBody ReactionDTO reactionDTO,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || ANONYMOUS_USER.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        reactionDTO.setCommentId(commentId);

        try {
            CommentDTO updated = commentService.addReaction(reactionDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping("/starred")
    public ResponseEntity<List<CommentDTO>> getStarredComments() {
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

    @PutMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<CommentDTO> editComment(
            @PathVariable String commentId,
            @RequestParam String newContent,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || ANONYMOUS_USER.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentDTO updated = commentService.editComment(commentId, newContent);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updated);
    }

    @DeleteMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated() || ANONYMOUS_USER.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
