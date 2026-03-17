package com.skillstack.devhub.repository;


import com.skillstack.devhub.model.Category;
import com.skillstack.devhub.model.Dificultad;
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
    List<Question> findByCategory(Category category);
    Page<Question> findByDifficulty (Dificultad difficulty, Pageable pageable);
}


