package com.clicks.fulafiaquizapp.model;

import com.clicks.fulafiaquizapp.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exam {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @OneToOne
    private Course course;
    private Byte noOfQuestions;
    private Double capScore;
    private Integer noOfAnswerableQuestion;
    @OneToMany(mappedBy = "exam")
    private List<Question> questions;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime stopTime;
    @Enumerated(EnumType.STRING)
    private ExamStatus status;
}
