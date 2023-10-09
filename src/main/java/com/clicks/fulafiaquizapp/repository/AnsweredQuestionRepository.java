package com.clicks.fulafiaquizapp.repository;

import com.clicks.fulafiaquizapp.model.AnsweredQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, Long> {
}
