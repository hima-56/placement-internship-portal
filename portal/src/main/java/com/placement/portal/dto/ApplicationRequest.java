// dto/ApplicationRequest.java
package com.placement.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApplicationRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    // Either jobPostingId OR internshipPostingId must be provided
    private Long jobPostingId;
    private Long internshipPostingId;
}