// dto/InterviewRequest.java
package com.placement.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InterviewRequest {

    @NotNull(message = "Application ID is required")
    private Long applicationId;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledAt;

    private String mode;       // "Online", "In-person"
    private String feedback;
}