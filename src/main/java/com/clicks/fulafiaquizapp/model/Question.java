package com.clicks.fulafiaquizapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String question;

    @OneToOne
    private QuestionOption options;

    @ManyToOne
    @JoinColumn
    private Exam exam;

}
