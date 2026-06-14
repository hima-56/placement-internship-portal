// dto/JobOpportunityResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.EligibilityStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobOpportunityResponse {

    private JobPostingResponse jobPosting;

    private EligibilityStatus eligibilityStatus;

    // Frontend uses this to disable/enable the apply button
    private boolean canApply;

    // Shown as a message below the apply button
    private String eligibilityMessage;
}