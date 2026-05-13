package com.skillstack.devhub.model;

public class CommentReaction {
    Reaction reaction;
    String commentId;
    String userId;

    public CommentReaction(Reaction reaction, String commentId, String userId) {
        this.reaction = reaction;
        this.commentId = commentId;
        this.userId = userId;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public String getUserId() {
        return userId;
    }
}
