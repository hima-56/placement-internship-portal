// dto/InterviewResultRequest.java
package com.placement.portal.dto;

import com.placement.portal.model.InterviewResult;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InterviewResultRequest {

    @NotNull(message = "Result is required")
    private InterviewResult result;   // PASSED or FAILED

    private String feedback;
}