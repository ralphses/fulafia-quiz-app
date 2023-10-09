package com.clicks.fulafiaquizapp.repository;

import com.clicks.fulafiaquizapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT student FROM Student student WHERE student.matric = ?1")
    Optional<Student> findByMatric(String matric);
}
