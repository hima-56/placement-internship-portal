// dto/InternshipPostingResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.PostingStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InternshipPostingResponse {
    private Long id;
    private String title;
    private String description;
    private String eligibleBranches;
    private Double minCgpa;
    private String requiredSkills;
    private Double stipend;
    private boolean manualReviewEnabled;
    private Integer durationMonths;
    private String mode;
    private LocalDate startDate;
    private PostingStatus status;
    private Long companyId;
    private String companyName;
    private LocalDateTime createdAt;
}