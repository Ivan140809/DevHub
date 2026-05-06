package com.skillstack.devhub.model;

import com.skillstack.devhub.Observer.Observer;
import com.skillstack.devhub.Observer.Subject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "comments")
public class Comment implements Subject {

    @Id
    private String id;

    private String title;
    private String content;
    private String category;
    private List<String> tags;
    private String username;
    private String createdAt;
    private boolean isStarred;
    private int happyFace;
    private int sadFace;

    private List<Comment> replies = new ArrayList<>();

    // usernames de quienes están suscritos — si se persiste en MongoDB
    private List<String> subscribedUsernames = new ArrayList<>();

    // lista en memoria — no se persiste
    @Transient
    private transient List<Observer> observers = new ArrayList<>();

    public Comment() {
    }

    public Comment(String title, String content, String category, List<String> tags, String username, boolean isStarred, int happyFace, int sadFace) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.username = username;
        this.createdAt = Instant.now().toString();
        this.isStarred = isStarred;
        this.happyFace = happyFace;
        this.sadFace = sadFace;
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message, this);
        }
    }

    public void addReply(Comment reply) {
        replies.add(reply);
        notifyObservers("Nueva respuesta en el comentario de: " + username);
    }

    public void setContent(String content) {
        this.content = content;
        notifyObservers("El comentario de " + username + " fue editado");
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CommentComponent toComponent() {

        if (replies == null || replies.isEmpty()) {
            return new CommentLeaf(id, title, content, category, tags, username, createdAt, isStarred, happyFace, sadFace);
        }

        CommentComposite composite =
                new CommentComposite(id, title, content, category, tags, username, createdAt, isStarred, happyFace, sadFace);

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getHappyFace() {
        return happyFace;
    }

    public void setHappyFace(int happyFace) {
        this.happyFace = happyFace;
    }

    public int getSadFace() {
        return sadFace;
    }

    public void setSadFace(int sadFace) {
        this.sadFace = sadFace;
    }

    public List<String> getSubscribedUsernames() {
        return subscribedUsernames;
    }

    public void subscribe(String username) {
        if (!subscribedUsernames.contains(username)) {
            subscribedUsernames.add(username);
        }
    }

}