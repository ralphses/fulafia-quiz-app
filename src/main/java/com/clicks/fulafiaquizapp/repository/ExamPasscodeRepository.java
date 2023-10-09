package com.clicks.fulafiaquizapp.repository;

import com.clicks.fulafiaquizapp.model.ExamPassCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamPasscodeRepository extends JpaRepository<ExamPassCode, Long> {

    @Query(value = "SELECT code FROM ExamPassCode code WHERE code.code = ?1")
    Optional<ExamPassCode> findByCode(String code);

}
