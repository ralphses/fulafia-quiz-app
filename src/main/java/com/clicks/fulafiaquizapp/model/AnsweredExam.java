package com.clicks.fulafiaquizapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnsweredExam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Exam exam;

    private Double totalScore;

    @OneToMany
    private List<AnsweredQuestion> answeredQuestions;

    @ManyToOne
    private Student student;
}
