// model/InternshipPosting.java
package com.placement.portal.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "internship_postings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class InternshipPosting extends Posting {

    private Double stipend;          // Monthly stipend
    private Integer durationMonths;
    private String mode;             // "Remote", "Onsite", "Hybrid"
    private LocalDate startDate;
}