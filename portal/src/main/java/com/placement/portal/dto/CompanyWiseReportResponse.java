// dto/CompanyWiseReportResponse.java
package com.placement.portal.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompanyWiseReportResponse {
    private Long companyId;
    private String companyName;
    private long totalApplications;
    private long shortlisted;
    private long selected;
    private long rejected;
    private Double averageCtc;
    private Double averageStipend;
}
