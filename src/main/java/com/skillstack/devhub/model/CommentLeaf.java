package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

import java.util.List;

public class CommentLeaf extends CommentComponent{

    public CommentLeaf(String id, String title, String content, String category, List<String> tags, String username, boolean isStarred, int happyFace, int sadFace){
        super(id, title, content, category, tags, username, isStarred, happyFace, sadFace);
    }

    @Override
    public CommentDTO toDTO(){
        return new CommentDTO(id, title, content, category, tags, username, isStarred, List.of(), happyFace, sadFace);
    }


}
