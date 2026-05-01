package com.skillstack.devhub.repository;

import com.skillstack.devhub.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    Optional<Comment> findById(String id);

    List<Comment> findByUsername(String username);
    List<Comment> findByIsStarred(Boolean isStarred);
    List<Comment> findTop7ByOrderByHappyFaceDesc();
}
