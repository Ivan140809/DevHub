package com.skillstack.devhub.dto;

import com.skillstack.devhub.model.Reaction;

public class ReactionDTO {
    Reaction reaction;
    String commentId;
    String userId;

    public ReactionDTO(Reaction reaction, String commentId, String userId) {
        this.reaction = reaction;
        this.commentId = commentId;
        this.userId = userId;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserId() {
        return userId;
    }
}
