// dto/PlacementSummaryResponse.java
package com.placement.portal.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlacementSummaryResponse {
    private long totalStudents;
    private long totalPlaced;          // SELECTED
    private long totalUnplaced;
    private long totalApplications;
    private long totalCompanies;
    private long totalJobPostings;
    private long totalInternshipPostings;
    private double placementPercentage;
    private Double averageCtc;         // average CTC of selected job students
    private Double averageStipend;     // average stipend of selected internship students
}