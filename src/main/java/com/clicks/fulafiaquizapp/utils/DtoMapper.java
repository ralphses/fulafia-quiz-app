package com.clicks.fulafiaquizapp.utils;

import com.clicks.fulafiaquizapp.dto.*;
import com.clicks.fulafiaquizapp.model.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;

@Component
public class DtoMapper {

    public StudentDto studentDto(Student student) {
        return new StudentDto(
                student.getMatric(),
                student.getName(),
                student.getImageUrl(),
                student.getCourses().stream().map(this::courseDto).toList()
        );
    }

    public CourseDto courseDto(Course course) {
        return new CourseDto(
                course.getTitle(),
                course.getCode(),
                course.getUnit()
        );
    }

    public PassCodeDto passCodeDto(ExamPassCode passCode) {
        return new PassCodeDto(
                passCode.getCode(),
                passCode.getExpiresAt().isAfter(now())
        );
    }

    public ExamDto examDto(Exam exam, Student student) {

        List<Question> examQuestions = exam.getQuestions();
        Collections.shuffle(examQuestions);

        Duration duration = Duration.between(exam.getStartTime(), exam.getStopTime());

        return new ExamDto(
                exam.getId(),
                studentDto(student),
                courseDto(exam.getCourse()),
                examQuestions.stream().limit(exam.getNoOfAnswerableQuestion()).map(this::questionDto).toList(),
                exam.getDate().toString(),
                exam.getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME),
                exam.getStopTime().format(DateTimeFormatter.ISO_LOCAL_TIME),
                Math.abs(duration.getSeconds())
        );
    }

    public QuestionDto questionDto(Question question) {
        QuestionOption questionOptions = question.getOptions();
        return new QuestionDto(
                question.getId(),
                question.getQuestion(),
                questionOptions.getOptionA(),
                questionOptions.getOptionB(),
                questionOptions.getOptionC(),
                questionOptions.getOptionD());
    }
}
