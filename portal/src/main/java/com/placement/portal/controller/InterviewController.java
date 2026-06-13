// controller/InterviewController.java
package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    // Company schedules an interview (round auto-determined)
    @PostMapping
    public ResponseEntity<InterviewResponse> schedule(@Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewService.scheduleInterview(request));
    }

    // Company updates round result
    @PutMapping("/{id}/result")
    public ResponseEntity<InterviewResponse> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody InterviewResultRequest request) {
        return ResponseEntity.ok(
                interviewService.updateResult(id, request.getResult(), request.getFeedback()));
    }

    // Student views all their interviews
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InterviewResponse>> getForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(interviewService.getInterviewsForStudent(studentId));
    }

    // Full round history for an application
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponse>> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getByApplication(applicationId));
    }
}