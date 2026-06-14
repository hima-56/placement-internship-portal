// dto/InternshipOpportunityResponse.java
package com.placement.portal.dto;

import com.placement.portal.model.EligibilityStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InternshipOpportunityResponse {

    private InternshipPostingResponse internshipPosting;

    private EligibilityStatus eligibilityStatus;

    private boolean canApply;

    private String eligibilityMessage;
}