// controller/ReportController.java
package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // Overall placement summary
    @GetMapping("/summary")
    public ResponseEntity<PlacementSummaryResponse> getSummary() {
        return ResponseEntity.ok(reportService.getPlacementSummary());
    }

    // Branch-wise breakdown
    @GetMapping("/branch-wise")
    public ResponseEntity<List<BranchWiseReportResponse>> getBranchWise() {
        return ResponseEntity.ok(reportService.getBranchWiseReport());
    }

    // Company-wise breakdown
    @GetMapping("/company-wise")
    public ResponseEntity<List<CompanyWiseReportResponse>> getCompanyWise() {
        return ResponseEntity.ok(reportService.getCompanyWiseReport());
    }

    // Per-student placement status
    @GetMapping("/students")
    public ResponseEntity<List<StudentPlacementReportResponse>> getStudentReport() {
        return ResponseEntity.ok(reportService.getStudentPlacementReport());
    }

    // Interview success rates
    @GetMapping("/interviews")
    public ResponseEntity<List<InterviewReportResponse>> getInterviewReport() {
        return ResponseEntity.ok(reportService.getInterviewReport());
    }
}