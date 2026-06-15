// dto/InterviewReportResponse.java
package com.placement.portal.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewReportResponse {
    private Long companyId;
    private String companyName;
    private String postingTitle;
    private long totalInterviews;
    private long passed;
    private long failed;
    private long pending;
    private double passRate;
}