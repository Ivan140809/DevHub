package com.skillstack.devhub.controller;

import com.skillstack.devhub.dto.CommentDTO;
import com.skillstack.devhub.dto.CreateCommentRequest;
import com.skillstack.devhub.dto.CreateReplyRequest;
import com.skillstack.devhub.dto.ReactionDTO;
import com.skillstack.devhub.model.Reaction;
import com.skillstack.devhub.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @RequestBody CreateCommentRequest request,
            Principal principal) {
        CommentDTO created = commentService.createComment(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                request.getTags(),
                principal.getName(),
                false, 0, 0);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.getCommentTree(commentId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{commentId:[0-9a-f]{24}}/replies")
    public ResponseEntity<CommentDTO> addReply(
            @PathVariable String commentId,
            @RequestBody CreateReplyRequest request,
            Principal principal) {
        CommentDTO updated = commentService.addReply(commentId, request.getContent(), principal.getName(), false);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{commentId:[0-9a-f]{24}}/reactions")
    public ResponseEntity<CommentDTO> addReaction(
            @PathVariable String commentId,
            @RequestParam Reaction reaction,
            Principal principal) {
        ReactionDTO reactionDTO = new ReactionDTO(reaction, commentId, principal.getName());
        try {
            return ResponseEntity.ok(commentService.addReaction(reactionDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/starred")
    public ResponseEntity<List<CommentDTO>> getStarredComments() {
        return ResponseEntity.ok(commentService.getStarredComments());
    }

    @GetMapping("/top")
    public ResponseEntity<List<CommentDTO>> getCommentsMostReactions() {
        return ResponseEntity.ok(commentService.getCommentsMostReactions());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<CommentDTO> editComment(
            @PathVariable String commentId,
            @RequestParam String newContent) {
        return ResponseEntity.ok(commentService.editComment(commentId, newContent));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId:[0-9a-f]{24}}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
