package com.clicks.fulafiaquizapp.dto;

import java.util.List;

public record ExamSubmitRequest(

        Long examId,
        String courseCode,
        boolean finished,
        List<AnsweredQuestionDto> questions

) {
}
