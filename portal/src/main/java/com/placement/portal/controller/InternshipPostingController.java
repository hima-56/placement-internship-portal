// controller/InternshipPostingController.java
package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.service.InternshipPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/internship-postings")
@RequiredArgsConstructor
public class InternshipPostingController {

    private final InternshipPostingService internshipPostingService;

    @PostMapping
    public ResponseEntity<InternshipPostingResponse> create(@Valid @RequestBody InternshipPostingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(internshipPostingService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<InternshipPostingResponse>> getAll() {
        return ResponseEntity.ok(internshipPostingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternshipPostingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(internshipPostingService.getById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<InternshipPostingResponse>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(internshipPostingService.getByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternshipPostingResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody InternshipPostingRequest request) {
        return ResponseEntity.ok(internshipPostingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        internshipPostingService.delete(id);
        return ResponseEntity.noContent().build();
    }
    // Student sees all jobs they are eligible for
    @GetMapping("/eligible/{studentId}")
    public ResponseEntity<List<InternshipPostingResponse>> getEligible(@PathVariable Long studentId) {
        return ResponseEntity.ok(internshipPostingService.getEligibleInternships(studentId));
    }}