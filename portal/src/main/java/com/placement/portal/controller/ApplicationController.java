// controller/ApplicationController.java
package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(@Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.apply(request));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll() {
        return ResponseEntity.ok(applicationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ApplicationResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(applicationService.getByStudent(studentId));
    }

    @GetMapping("/job/{jobPostingId}")
    public ResponseEntity<List<ApplicationResponse>> getByJobPosting(@PathVariable Long jobPostingId) {
        return ResponseEntity.ok(applicationService.getByJobPosting(jobPostingId));
    }

    @GetMapping("/internship/{internshipPostingId}")
    public ResponseEntity<List<ApplicationResponse>> getByInternshipPosting(@PathVariable Long internshipPostingId) {
        return ResponseEntity.ok(applicationService.getByInternshipPosting(internshipPostingId));
    }
}