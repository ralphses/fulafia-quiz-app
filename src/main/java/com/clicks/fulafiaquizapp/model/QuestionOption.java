package com.clicks.fulafiaquizapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
}
