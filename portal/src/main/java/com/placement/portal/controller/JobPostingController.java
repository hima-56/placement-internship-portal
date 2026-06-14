// controller/JobPostingController.java
package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;


    @PostMapping
    public ResponseEntity<JobPostingResponse> create(@Valid @RequestBody JobPostingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPostingService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<JobPostingResponse>> getAll() {
        return ResponseEntity.ok(jobPostingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostingService.getById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobPostingResponse>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobPostingService.getByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostingResponse> update(@PathVariable Long id,
                                                     @Valid @RequestBody JobPostingRequest request) {
        return ResponseEntity.ok(jobPostingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobPostingService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/eligible/{studentId}")
    public ResponseEntity<List<JobOpportunityResponse>> getOpportunities(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(jobPostingService.getOpportunitiesForStudent(studentId));
    }
}