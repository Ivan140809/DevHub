package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

public abstract class CommentComponent {

    protected String id;
    protected String content;
    protected String username;
    protected boolean isStarred;

    public CommentComponent(String id, String content, String username, boolean isStarred){
        this.id = id;
        this.content= content;
        this.username = username;
        this.isStarred = isStarred;
    }

    public abstract CommentDTO toDTO();

}
