// dto/JobPostingRequest.java
package com.placement.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class JobPostingRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String eligibleBranches;   // "CSE,ECE,IT"
    private Double minCgpa;
    private boolean manualReviewEnabled;

    private String description;
    private String requiredSkills;
    private Double ctc;
    private String jobType;
    private String location;
    private LocalDate deadline;

    @NotNull(message = "Company ID is required")
    private Long companyId;
}