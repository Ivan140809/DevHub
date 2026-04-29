package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

import java.util.List;

public class CommentLeaf extends CommentComponent{

    public CommentLeaf(String id, String content, String username, boolean isStarred, int happyFace, int sadFace){
        super(id,content, username, isStarred, happyFace, sadFace);
    }

    @Override
    public CommentDTO toDTO(){
        return new CommentDTO(id,content, username, isStarred,List.of(), happyFace, sadFace);
    }


}
