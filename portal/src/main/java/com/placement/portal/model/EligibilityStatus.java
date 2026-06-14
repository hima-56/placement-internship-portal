// model/EligibilityStatus.java
package com.placement.portal.model;

public enum EligibilityStatus {
    ELIGIBLE,           // branch matches + cgpa meets cutoff
    CGPA_BELOW_CUTOFF,  // branch matches but cgpa is low
    NOT_ELIGIBLE        // branch doesn't match at all
}