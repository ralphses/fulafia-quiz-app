package com.clicks.fulafiaquizapp.service;

import com.clicks.fulafiaquizapp.dto.StudentDto;
import com.clicks.fulafiaquizapp.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaquizapp.model.Student;
import com.clicks.fulafiaquizapp.repository.StudentRepository;
import com.clicks.fulafiaquizapp.utils.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DtoMapper mapper;

    /**
     * Retrieves a student's information by their matriculation number.
     *
     * @param matric The matriculation number of the student.
     * @return A StudentDto containing the student's details.
     * @throws ResourceNotFoundException if the student with the given matriculation number is not found.
     */
    public StudentDto getByMatric(String matric) {
        // Step 1: Retrieve the student by their matriculation number using the getStudent method.
        Student student = getStudent(matric);

        // Step 2: Map the Student entity to a StudentDto and return it.
        return mapper.studentDto(student);
    }


    /**
     * Retrieves a student by their matriculation number.
     *
     * @param matric The matriculation number of the student.
     * @return The Student entity corresponding to the matriculation number.
     * @throws ResourceNotFoundException if no student is found with the given matriculation number.
     */
    public Student getStudent(String matric) {
        // Step 1: Retrieve the student by their matriculation number from the repository.
        return studentRepository.findByMatric(matric)
                .orElseThrow(() -> new ResourceNotFoundException("Student with matric number " + matric + " NOT FOUND"));
    }

}
