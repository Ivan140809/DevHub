package com.skillstack.devhub.repository;

import com.skillstack.devhub.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findByQuestionId(String questionID, Pageable pageable);
}
