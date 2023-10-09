package com.clicks.fulafiaquizapp.repository;

import com.clicks.fulafiaquizapp.model.AnsweredExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnsweredExamRepository extends JpaRepository<AnsweredExam, Long> {
}
