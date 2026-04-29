package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

import java.util.ArrayList;
import java.util.List;

public class CommentComposite extends CommentComponent{

    private List<CommentComponent> replies = new ArrayList<>();

    public CommentComposite(String id, String content, String username, boolean isStarred, int happyFace, int sadFace){
        super(id, content,username, isStarred, happyFace, sadFace);
    }

    public void add(CommentComponent c){
        replies.add(c);
    }

    @Override
    public CommentDTO toDTO(){
        return new CommentDTO(
                id,
                content,
                username,
                isStarred,
                replies.stream().map(CommentComponent::toDTO).toList(),
                happyFace,
                sadFace
        );
    }

}
