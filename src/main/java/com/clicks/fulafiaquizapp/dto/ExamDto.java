package com.clicks.fulafiaquizapp.dto;

import java.util.List;

public record ExamDto(
        Long id,
        StudentDto student,
        CourseDto course,
        List<QuestionDto> questions,
        String date,
        String startTime,
        String stopTime,
        long duration
) {
}
