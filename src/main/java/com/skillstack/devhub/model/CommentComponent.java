package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

public abstract class CommentComponent {

    protected String id;
    protected String content;
    protected String username;

    public CommentComponent(String id, String content, String username){
        this.id = id;
        this.content= content;
        this.username = username;
    }

    public abstract CommentDTO toDTO();

}
