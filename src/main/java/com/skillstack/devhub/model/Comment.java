package com.skillstack.devhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String content;
    private String username;

    private List<Comment> replies = new ArrayList<>();

    public Comment() {}

    public Comment(String content, String username) {
        this.content = content;
        this.username = username;
    }

    public CommentComponent toComponent() {

        if (replies == null || replies.isEmpty()) {
            return new CommentLeaf(id, content, username);
        }

        CommentComposite composite =
                new CommentComposite(id, content, username);

        for (Comment child : replies) {
            composite.add(child.toComponent());
        }

        return composite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }
}