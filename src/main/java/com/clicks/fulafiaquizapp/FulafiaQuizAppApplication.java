package com.clicks.fulafiaquizapp;

import com.clicks.fulafiaquizapp.dto.NewQuestionDto;
import com.clicks.fulafiaquizapp.enums.ExamStatus;
import com.clicks.fulafiaquizapp.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaquizapp.model.*;
import com.clicks.fulafiaquizapp.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;

@SpringBootApplication
public class FulafiaQuizAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FulafiaQuizAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            QuestionRepository questionRepository,
            QuestionOptionRepository questionOptionRepository,
            ExamRepository examRepository
    ) {
        return args -> {
            Course course1 = courseRepository.save(
                    Course.builder()
                            .code("GST111")
                            .title("Use of English I")
                            .unit((byte) 2)
                            .build()
            );

            Course course2 = courseRepository.save(
                    Course.builder()
                            .code("GST121")
                            .title("Use of English II")
                            .unit((byte) 2)
                            .build()
            );

            Course course3 = courseRepository.save(
                    Course.builder()
                            .code("CSC111")
                            .title("Introduction to Computer Science")
                            .unit((byte) 3)
                            .build()
            );

            Course course4 = courseRepository.save(
                    Course.builder()
                            .code("CSC121")
                            .title("Introduction to Algorithms and problem solving")
                            .unit((byte) 3)
                            .build()
            );
            Student student1 = studentRepository.save(
                    Student.builder()
                            .name("Christopher Emmanuel")
                            .matric("2031800106")
                            .courses(List.of(course1, course4, course3))
                            .build()
            );

            Student student2 = studentRepository.save(
                    Student.builder()
                            .name("Fidelis Eze")
                            .matric("2031800111")
                            .courses(List.of(course1, course2, course4, course3))
                            .build()
            );

            Student student3 = studentRepository.save(
                    Student.builder()
                            .name("Godwin Michael")
                            .matric("2031800115")
                            .courses(List.of(course1, course2))
                            .build()
            );

            List<String> courseFiles = List.of("gst111", "csc121", "csc111", "gst121");
            List<List<Question>> questions = new ArrayList<>();

            for (String course : courseFiles) {

                Exam exam = examRepository.save(Exam.builder()
                        .noOfQuestions((byte) 10)
                        //hh
                        .noOfAnswerableQuestion(8)
                        .capScore(40.0)
                        .status(ExamStatus.CREATED)
                        .date(now())
                        .course(courseRepository.findByCode(course).orElseThrow(() -> new ResourceNotFoundException("NOT FOUND")))
                        .startTime(LocalTime.now())
                        .stopTime(LocalTime.now().plusHours(1))
                        .build());

                try {
                    List<Question> courseQuestion = getQuestions(course)
                            .stream()
                            .map(co -> questionRepository.save(
                                    Question.builder()
                                            .options(
                                                    questionOptionRepository.save(
                                                            QuestionOption.builder()
                                                                    .correctOption(co.correct())
                                                                    .optionD(co.optionD())
                                                                    .optionA(co.optionA())
                                                                    .optionC(co.optionC())
                                                                    .optionB(co.optionB())
                                                                    .build()))
                                            .question(co.question())
                                            .exam(exam)
                                            .build()
                            ))
                            .toList();

                    questions.add(courseQuestion);
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to read file questions for " + course);
                }

            }

            questions.forEach(questionRepository::saveAll);
        };
    }

    private List<NewQuestionDto> getQuestions(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("questions/" + fileName + ".json");
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
        });
    }
}
