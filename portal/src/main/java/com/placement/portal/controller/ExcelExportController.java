// controller/ExcelExportController.java
package com.placement.portal.controller;

import com.placement.portal.service.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExcelExportController {

    private final ExcelExportService excelExportService;

    // Full report — all 5 sheets in one workbook
    @GetMapping("/full-report")
    public ResponseEntity<InputStreamResource> exportFullReport() {
        try {
            ByteArrayInputStream data = excelExportService.exportFullReport();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment; filename=placement_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(data));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}