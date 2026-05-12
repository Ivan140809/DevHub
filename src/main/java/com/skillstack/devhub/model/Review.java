package com.skillstack.devhub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private String comment;
    private int rating;
    private LocalDate date;
    private String questionId;
    private String userId;

    public Review() {}

    public Review(String comment, int rating, String questionId, String userId, LocalDate date) {
        this.comment = comment;
        this.rating = rating;
        this.questionId = questionId;
        this.userId = userId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
