package com.clicks.fulafiaquizapp.dto;

public record NewQuestionDto(

        String question,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        String correct
) {
}
