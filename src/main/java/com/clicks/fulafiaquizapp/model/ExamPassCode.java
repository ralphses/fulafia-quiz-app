package com.clicks.fulafiaquizapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamPassCode {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    LocalDateTime expiresAt;
    private boolean used;

    @ManyToOne
    private Student student;
}
