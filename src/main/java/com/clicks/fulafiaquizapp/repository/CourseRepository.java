package com.clicks.fulafiaquizapp.repository;

import com.clicks.fulafiaquizapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT course FROM Course course WHERE course.code = ?1")
    Optional<Course> findByCode(String code);
}
