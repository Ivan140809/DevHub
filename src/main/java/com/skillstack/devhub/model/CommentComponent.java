package com.skillstack.devhub.model;

import java.util.List;

import com.skillstack.devhub.dto.CommentDTO;

public abstract class CommentComponent {

    protected String id;
    protected String title;
    protected String content;
    protected String category;
    protected List<String> tags;
    protected String username;
    protected boolean isStarred;
    protected int happyFace;
    protected int sadFace;

    public CommentComponent(String id, String title, String content, String category, List<String> tags, String username, boolean isStarred, int happyFace, int sadFace){
        this.id = id;
        this.title = title;
        this.content= content;
        this.category = category;
        this.tags = tags;
        this.username = username;
        this.isStarred = isStarred;
        this.happyFace = happyFace;
        this.sadFace=sadFace;
    }

    public abstract CommentDTO toDTO();

}
