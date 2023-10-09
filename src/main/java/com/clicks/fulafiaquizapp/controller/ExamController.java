package com.clicks.fulafiaquizapp.controller;

import com.clicks.fulafiaquizapp.dto.ExamDto;
import com.clicks.fulafiaquizapp.dto.ExamSubmitRequest;
import com.clicks.fulafiaquizapp.service.ExamService;
import com.clicks.fulafiaquizapp.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/exam")
public class ExamController {

    private final ExamService examService;

    /**
     * Retrieves exam information for a student using a passcode.
     *
     * @param passcode The passcode associated with the exam.
     * @return An ExamDto containing exam details if the passcode is valid and unused.
     * @throws com.clicks.fulafiaquizapp.exceptions.ResourceNotFoundException if the passcode is invalid.
     * @throws com.clicks.fulafiaquizapp.exceptions.InvalidParamsException if the passcode has already been used.
     */
    @GetMapping("/get")
    public ResponseEntity<CustomResponse> getExam(
            @RequestParam(name = "passcode") String passcode,
            @RequestParam(name = "matric") String matric) {
        // Delegate the retrieval of exam information to the examService.
        ExamDto studentExams = examService.getStudentExams(passcode, matric);
        return ResponseEntity.ok(new CustomResponse("Success", studentExams));
    }


    /**
     * Handles the submission of an exam for a student.
     *
     * @param matric The matriculation number of the student.
     * @param examSubmitRequest The request body containing the exam submission details.
     * @return True if the exam submission is successful, false otherwise.
     */
    @PostMapping("/submit/{matric}")
    public ResponseEntity<CustomResponse> submit(@PathVariable String matric, @RequestBody ExamSubmitRequest examSubmitRequest) {
        // Delegate the submission logic to the examService.
        boolean submit = examService.submit(matric, examSubmitRequest);
        return ResponseEntity.ok(new CustomResponse("SUCCESS", submit));
    }

    /**
     * Initiates the start of an exam for a specified course.
     *
     * @param course The code of the course for which the exam is to be started.
     * @return True if the exam is successfully started, false otherwise.
     */
    @PostMapping("/start/{course}")
    public ResponseEntity<CustomResponse> startExam(@PathVariable String course) {
        // Delegate the exam starting logic to the examService.
        boolean start = examService.start(course);
        return ResponseEntity.ok(new CustomResponse("SUCCESS", start));
    }



}
