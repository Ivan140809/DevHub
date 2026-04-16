package com.skillstack.devhub.Observer;

import com.skillstack.devhub.model.Comment;

public interface Observer {
    void update(String message, Comment comment);
}
