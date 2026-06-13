// dto/InterviewResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.InterviewResult;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InterviewResponse {
    private Long id;
    private Long applicationId;
    private Long studentId;
    private String studentName;
    private String postingTitle;
    private String companyName;
    private Integer roundNumber;
    private LocalDateTime scheduledAt;
    private String mode;
    private InterviewResult result;
    private String feedback;
}