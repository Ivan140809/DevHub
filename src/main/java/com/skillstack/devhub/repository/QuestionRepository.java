package com.skillstack.devhub.repository;


import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Difficulty;
import com.skillstack.devhub.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findByTitle(String title);
    Page<Question> findByCategory(Category category, Pageable pageable);
    Page<Question> findByDifficulty(Difficulty difficulty, Pageable pageable);
}


