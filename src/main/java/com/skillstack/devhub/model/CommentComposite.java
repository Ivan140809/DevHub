package com.skillstack.devhub.model;

import com.skillstack.devhub.dto.CommentDTO;

import java.util.ArrayList;
import java.util.List;

public class CommentComposite extends CommentComponent{

    private List<CommentComponent> replies = new ArrayList<>();

    public CommentComposite(String id,String content, String username, boolean isStarred){
        super(id, content,username, isStarred);
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
                replies.stream().map(CommentComponent::toDTO).toList()
        );
    }

}
