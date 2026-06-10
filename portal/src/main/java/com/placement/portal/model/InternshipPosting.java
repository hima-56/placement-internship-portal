// model/InternshipPosting.java
package com.placement.portal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "internship_postings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InternshipPosting extends Posting {

    private Double stipend;          // Monthly stipend
    private Integer durationMonths;
    private String mode;             // "Remote", "Onsite", "Hybrid"
    private LocalDate startDate;
}