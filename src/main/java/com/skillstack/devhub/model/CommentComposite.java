package com.skillstack.devhub.model;

import java.util.ArrayList;
import java.util.List;

import com.skillstack.devhub.dto.CommentDTO;

public class CommentComposite extends CommentComponent{

    private final List<CommentComponent> replies = new ArrayList<>();

    public CommentComposite(String id, String title, String content, String category, List<String> tags, String username, String createdAt, boolean isStarred, int happyFace, int sadFace){
        super(id, title, content, category, tags, username, createdAt, isStarred, happyFace, sadFace);
    }

    public void add(CommentComponent c){
        replies.add(c);
    }

    @Override
    public CommentDTO toDTO(){
        return new CommentDTO(
                id,
                title,
                content,
                category,
                tags,
                username,
                createdAt,
                isStarred,
                replies.stream().map(CommentComponent::toDTO).toList(),
                happyFace,
                sadFace
        );
    }

}
