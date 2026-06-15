// dto/StudentPlacementReportResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.ApplicationStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentPlacementReportResponse {
    private Long studentId;
    private String studentName;
    private String branch;
    private Double cgpa;
    private ApplicationStatus applicationStatus;
    private String placedAt;           // company name if selected
    private String placedRole;         // job/internship title if selected
    private String placementType;      // "JOB" or "INTERNSHIP"
    private Double ctcOrStipend;
}