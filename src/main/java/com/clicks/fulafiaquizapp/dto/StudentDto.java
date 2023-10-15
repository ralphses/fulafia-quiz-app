package com.clicks.fulafiaquizapp.dto;

import java.util.List;

public record StudentDto(

        String matric,
        String name,
        String image,
        //comment
        List<CourseDto> courses
) {
}
