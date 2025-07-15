package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.StudentMarksRequestDTO;
import com.vigyanmancha.backend.service.StudentMarksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Student Marks API", description = "Endpoints for managing marks awarded to students for specific subjects.")
@RestController
@RequestMapping("/api/student-marks")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StudentMarksController {
    private final StudentMarksService service;

    @Operation(summary = "Create a new StudentMarks record", description = "Create a student marks record by specifying student, subject, and marks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created",
                    content = @Content(schema = @Schema(implementation = StudentMarksRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<StudentMarksRequestDTO> createMarks(
            @Valid @RequestBody
            @Parameter(description = "StudentMarks DTO with studentId, subjectId, marks, and maximumMarks")
            StudentMarksRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Get all StudentMarks", description = "Returns a list of all student marks records.")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    @GetMapping
    public ResponseEntity<List<StudentMarksRequestDTO>> getAllMarks() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get a StudentMarks record by ID", description = "Returns the student marks record for the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Record found"),
            @ApiResponse(responseCode = "404", description = "Record not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentMarksRequestDTO> getMarksById(
            @Parameter(description = "ID of the StudentMarks record to retrieve")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Delete a StudentMarks record by ID", description = "Deletes the specified student marks record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Record not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(
            @Parameter(description = "ID of the StudentMarks record to delete")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


