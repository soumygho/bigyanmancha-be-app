package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.annotation.AdminUser;
import com.vigyanmancha.backend.dto.request.StudentRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student enrollment api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class StudentController {


    private final StudentService studentService;

    @GetMapping(produces = "application/json")
    @AdminOrVigyankendraUser
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping(path = "/vigyan-kendra/{id}", produces = "application/json")
    @AdminUser
    public List<StudentResponseDto> getAllStudentsByVigyanKendraId(@PathVariable Long id) {
        return studentService.getAllByVigyanKendraId(id);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @AdminOrVigyankendraUser
    public StudentResponseDto createStudent(@RequestBody StudentRequestDTO studentDTO) {
        return studentService.create(studentDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    @AdminOrVigyankendraUser
    public StudentResponseDto updateStudent(@RequestBody StudentRequestDTO studentDTO) {
        return studentService.update(studentDTO);
    }

    @DeleteMapping(path = "/{id}")
    @AdminOrVigyankendraUser
    public void deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
    }


    /*@GetMapping(path = "/generate")
    public String generateDummyData() {
        studentService.generateDummyData();
        return "Started generating the data";
    }*/

    @GetMapping(path = "/assign-roll-number/{classId}")
    public String assignRollNumber(@PathVariable("classId") Long classId) {
        studentService.generateDummyData();
        return "Started generating the data";
    }
}

