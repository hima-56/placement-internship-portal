// dto/InternshipPostingRequest.java
package com.placement.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InternshipPostingRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String eligibleBranches;
    private Double minCgpa;

    private String description;
    private String requiredSkills;
    private Double stipend;
    private Integer durationMonths;
    private String mode;
    private LocalDate startDate;

    @NotNull(message = "Company ID is required")
    private Long companyId;
}