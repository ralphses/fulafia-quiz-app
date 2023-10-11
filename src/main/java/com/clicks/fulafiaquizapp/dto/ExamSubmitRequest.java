package com.clicks.fulafiaquizapp.dto;

import java.util.List;

public record ExamSubmitRequest(

        Long examId,
        List<AnsweredQuestionDto> questions

) {
}
