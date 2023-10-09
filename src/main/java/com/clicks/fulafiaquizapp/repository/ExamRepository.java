package com.clicks.fulafiaquizapp.repository;

import com.clicks.fulafiaquizapp.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query(value = "SELECT exam FROM Exam exam WHERE exam.course.code = ?1")
    Optional<Exam> findByCourseCode(String code);
}
