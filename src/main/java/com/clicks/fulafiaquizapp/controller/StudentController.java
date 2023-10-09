package com.clicks.fulafiaquizapp.controller;

import com.clicks.fulafiaquizapp.dto.StudentDto;
import com.clicks.fulafiaquizapp.service.StudentService;
import com.clicks.fulafiaquizapp.utils.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/student")
public class StudentController {

    private final StudentService studentService;
    @GetMapping(value = "/{matric}")
    public ResponseEntity<CustomResponse> get(@PathVariable String matric) {
        StudentDto studentDto = studentService.getByMatric(matric);
        return ResponseEntity.ok(new CustomResponse("success", studentDto));
    }
}
