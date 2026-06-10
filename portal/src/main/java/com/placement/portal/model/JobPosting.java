// model/JobPosting.java
package com.placement.portal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_postings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPosting extends Posting {

    private Double ctc;              // Cost to company in LPA
    private String jobType;          // "Full-time", "Part-time"
    private String location;
    private LocalDate deadline;
}