// dto/JobPostingResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.PostingStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPostingResponse {
    private Long id;
    private String title;
    private String description;
    private String eligibleBranches;
    private Double minCgpa;
    private String requiredSkills;
    private Double ctc;
    private String jobType;
    private String location;
    private LocalDate deadline;
    private boolean manualReviewEnabled;
    private PostingStatus status;
    private Long companyId;
    private String companyName;
    private LocalDateTime createdAt;
}