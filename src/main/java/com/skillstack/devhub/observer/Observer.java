package com.skillstack.devhub.observer;

import com.skillstack.devhub.model.Comment;

public interface Observer {
    void update(String message, Comment comment);
}
