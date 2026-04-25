package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

public abstract class CommentComponent {

    protected String id;
    protected String content;
    protected String username;
    protected boolean isStarred;
    protected int happyFace;
    protected int sadFace;

    public CommentComponent(String id, String content, String username, boolean isStarred, int happyFace, int sadFace){
        this.id = id;
        this.content= content;
        this.username = username;
        this.isStarred = isStarred;
        this.happyFace = happyFace;
        this.sadFace=sadFace;
    }

    public abstract CommentDTO toDTO();

}
