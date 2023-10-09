package com.clicks.fulafiaquizapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String title;
    private String code;
    private Byte unit;
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<Student> students;
}
