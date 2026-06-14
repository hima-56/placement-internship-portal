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
    // Shortlist a single application
    @PutMapping("/{id}/shortlist")
    public ResponseEntity<ApplicationResponse> shortlist(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.shortlist(id));
    }

    // Bulk shortlist by job posting + cgpa cutoff
    @PutMapping("/job/{jobPostingId}/bulk-shortlist")
    public ResponseEntity<List<ApplicationResponse>> bulkShortlist(
            @PathVariable Long jobPostingId,
            @RequestParam Double minCgpa) {
        return ResponseEntity.ok(applicationService.bulkShortlistByJob(jobPostingId, minCgpa));
    }

    // Reject an application
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApplicationResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.reject(id));
    }

    // Mark as selected (final placement)
    @PutMapping("/{id}/select")
    public ResponseEntity<ApplicationResponse> select(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.markSelected(id));
    }
}