// dto/ApplicationResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.ApplicationStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long jobPostingId;
    private Long internshipPostingId;
    private String postingTitle;
    private String postingType;     // "JOB" or "INTERNSHIP"
    private ApplicationStatus status;
    private LocalDate appliedOn;
    private LocalDateTime createdAt;
}