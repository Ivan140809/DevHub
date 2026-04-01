package com.skillstack.devhub.repository;

import com.skillstack.devhub.model.Answer;
import com.skillstack.devhub.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {
    List<Answer> findByUserId(String UserId);
}
