// dto/BranchWiseReportResponse.java
package com.placement.portal.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BranchWiseReportResponse {
    private String branch;
    private long totalStudents;
    private long placed;
    private long unplaced;
    private double placementPercentage;
    private Double averageCgpa;
}