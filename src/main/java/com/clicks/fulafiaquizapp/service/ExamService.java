package com.clicks.fulafiaquizapp.service;

import com.clicks.fulafiaquizapp.dto.ExamDto;
import com.clicks.fulafiaquizapp.dto.ExamSubmitRequest;
import com.clicks.fulafiaquizapp.dto.QuestionDto;
import com.clicks.fulafiaquizapp.exceptions.InvalidParamsException;
import com.clicks.fulafiaquizapp.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaquizapp.model.*;
import com.clicks.fulafiaquizapp.repository.*;
import com.clicks.fulafiaquizapp.utils.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.clicks.fulafiaquizapp.enums.ExamStatus.CREATED;
import static com.clicks.fulafiaquizapp.enums.ExamStatus.ONGOING;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExamService {

    private final DtoMapper mapper;
    private final StudentService studentService;
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final ExamPasscodeRepository passcodeRepository;
    private final AnsweredExamRepository answeredExamRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final AnsweredQuestionRepository answeredQuestionRepository;

    /**
     * Adds a new exam based on the provided ExamDto.
     *
     * @param examDto The DTO containing the exam details.
     * @throws ResourceNotFoundException if the associated course is not found.
     * @throws InvalidParamsException    if there's an issue with the date or time format.
     */
    public void add(ExamDto examDto) {
        // Step 1: Retrieve the course by its code or throw an exception if it's not found.
        Course course = courseRepository.findByCode(examDto.course().code())
                .orElseThrow(() -> new ResourceNotFoundException("Course with code " + examDto.course() + " NOT FOUND"));

        try {
            // Step 2: Parse the date and time from the provided strings.
            LocalDate examDate = LocalDate.parse(examDto.date());
            LocalTime startTime = LocalTime.parse(examDto.startTime());
            LocalTime stopTime = LocalTime.parse(examDto.stopTime());

            // Step 3: Map the questionId DTOs to actual Question entities and collect them in a list.
            List<Question> questions = examDto.questions()
                    .stream()
                    .map(this::addQuestion)
                    .toList();

            // Step 4: Create a new Exam object with the gathered information.
            Exam newExam = Exam.builder()
                    .course(course)
                    .noOfQuestions((byte) questions.size())
                    .date(examDate)
                    .startTime(startTime)
                    .stopTime(stopTime)
                    .questions(questions)
                    .build();

            // Step 5: Save the new exam to the repository.
            examRepository.save(newExam);
        } catch (DateTimeParseException e) {
            // Step 6: Handle the exception if there's an issue with the date or time format.
            throw new InvalidParamsException("Either date or time passed is not valid. " +
                    "Date format should be YYYY-MM-DD and time format should be HH:MM");
        }
    }


    /**
     * Retrieves exam information for a student using a passcode.
     *
     * @param passcode The passcode associated with the exam.
     * @param matric The matriculation number of the student
     * @return An ExamDto containing exam details if the passcode is valid and unused.
     * @throws ResourceNotFoundException if the passcode is invalid.
     * @throws InvalidParamsException    if the passcode has already been used.
     */
    public ExamDto getStudentExams(String passcode, String matric) {

        // Step 1: Verify the validity of the passcode.
        ExamPassCode passCode = passcodeRepository.findByCode(passcode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid passcode " + passcode + " supplied"));

        Student student = studentService.getStudent(matric);
        Exam exam = passCode.getExam();

        if(!student.getCourses().contains(exam.getCourse())) {
            throw new InvalidParamsException(matric + " did not register for " + exam.getCourse().getCode());
        }

        if(student.getAnsweredExams()
                .stream()
                .anyMatch(answeredExam -> answeredExam.getExam().getId().equals(exam.getId()))){
            throw new InvalidParamsException(matric + " already sat for " + exam.getCourse().getCode());
        }

        // Step 2: Check if the passcode has already been used.
        if (!passCode.isUsed()) {

            passCode.setStudent(student);
            student.getPassCode().add(passCode);

            // Mark the passcode as used.
            passCode.setUsed(true);

            // Step 3: Map the exam associated with the passcode to an ExamDto and return it.
            return mapper.examDto(exam, student);
        } else {
            // Step 4: If the passcode has already been used, throw an exception.
            throw new InvalidParamsException("Passcode " + passcode + " has already been used");
        }
    }


    /**
     * Adds a new questionId to the system.
     *
     * @param questionDto The DTO containing the questionId details.
     * @return The newly added questionId.
     */
    public Question addQuestion(QuestionDto questionDto) {
        // Step 1: Create a QuestionOption object to represent the selectedOption options.
        QuestionOption options = questionOptionRepository.save(QuestionOption.builder()
                .optionD(questionDto.D())
                .optionA(questionDto.A())
                .optionB(questionDto.B())
                .optionC(questionDto.C())
                .build());

        // Step 2: Create a new Question object with the provided questionId text and the options.
        Question newQuestion = Question.builder()
                .question(questionDto.question())
                .options(options)
                .build();

        // Step 3: Save the new questionId to the repository and return it.
        return questionRepository.save(newQuestion);
    }


    /**
     * Submits an exam for a student.
     *
     * @param matric            The matriculation number of the student.
     * @param examSubmitRequest The exam submission request containing student answers.
     * @return True if the exam submission is successful, false otherwise.
     * @throws InvalidParamsException if the student or exam is not found.
     */
    public boolean submit(String matric, ExamSubmitRequest examSubmitRequest) {
        // Step 1: Retrieve the student by matriculation number.
        Student student = studentService.getStudent(matric);

        // Step 2: Retrieve the exam by ID, or throw an exception if it's not found.
        Exam exam = examRepository
                .findById(examSubmitRequest.examId())
                .orElseThrow(() -> new InvalidParamsException("Exam with ID " + examSubmitRequest.examId() + " NOT FOUND"));

        // Calculate the score per questionId in the exam.
        double scorePerExam = exam.getCapScore() / exam.getNoOfAnswerableQuestion();

        // Create an AnsweredExam object to store the submission information.
        AnsweredExam answeredExam = AnsweredExam.builder()
                .student(student)
                .totalScore(0.0) // Initialize the total score to zero.
                .exam(exam)
                .build();

        List<Long> userAnsweredQuestions = new ArrayList<>();

        // Step 3: Process and record the student's answers to each questionId in the exam.
        List<AnsweredQuestion> answeredQuestions = examSubmitRequest.questions()
                .stream()
                .map(questionSubmission -> {
                    // Retrieve the questionId by ID or throw an exception if it's not found.
                    Question question = questionRepository.findById(questionSubmission.questionId())
                            .orElseThrow(() -> new InvalidParamsException("Question with ID " + questionSubmission.questionId() + " NOT FOUND"));

                    // Check if the submitted selectedOption is correct and update the total score.
                    if (question.getOptions().getCorrectOption().equalsIgnoreCase(questionSubmission.selectedOption())) {
                        answeredExam.setTotalScore(answeredExam.getTotalScore() + scorePerExam);
                    }

                    userAnsweredQuestions.add(question.getId());

                    // Create an AnsweredQuestion object to record the student's selectedOption to this questionId.
                    return answeredQuestionRepository.save(AnsweredQuestion.builder()
                            .answer(questionSubmission.selectedOption())
                            .question(question)
                            .build());
                })
                .toList();

        //Update questions for unanswered questions
        exam.getQuestions().forEach(e -> {
            if(!userAnsweredQuestions.contains(e.getId())) {
                answeredQuestionRepository.save(
                        AnsweredQuestion.builder()
                                .question(e)
                                .answer("F")
                                .build()
                );
            }
        });

        // Set the answered questions in the AnsweredExam.
        answeredExam.setAnsweredQuestions(answeredQuestions);

        // Step 4: Save the AnsweredExam to record the submission.
        answeredExamRepository.save(answeredExam);

        // Return true to indicate a successful submission.
        return true;
    }


    /**
     * Generates and starts an exam for a given course.
     *
     * @param courseCode The code of the course for which the exam is to be started.
     * @return True if the exam is successfully started, false otherwise.
     * @throws ResourceNotFoundException if the course or exam is not found.
     */
    public boolean start(String courseCode) {

        // Step 1: Find the course by its code or throw an exception if it's not found.
        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course with course code " + courseCode + " NOT FOUND"));

        // Step 2: Find the exam by course code or throw an exception if it's not found.
        Exam exam = examRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Exam for course with course code " + courseCode + " NOT FOUND"));

        if(!exam.getStatus().equals(CREATED)) {
            throw new InvalidParamsException("Exam for course with course code " + courseCode + " already started or completed");
        }

        // Step 3: Generate and save passcodes for each student in the course.
        course.getStudents().forEach(student -> {
            LocalDate examDate = exam.getDate();
            LocalTime examStopTime = exam.getStopTime();

            String code = courseCode + new Random().nextInt(10000, 999999);
            passcodeRepository.save(
                    ExamPassCode.builder()
                            .code(code)
                            .exam(exam)
                            .expiresAt(LocalDateTime.of(
                                    examDate.getYear(),
                                    examDate.getMonth(),
                                    examDate.getDayOfMonth(),
                                    examStopTime.getHour(),
                                    examStopTime.getMinute(),
                                    examStopTime.getSecond()).plusMinutes(2))
                            .used(false)
                            .build());

        });

        exam.setStatus(ONGOING);

        // Step 4: Return true to indicate a successful start.
        return true;
    }

}
