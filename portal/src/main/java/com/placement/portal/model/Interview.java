// model/Interview.java
package com.placement.portal.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Interview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    private Integer roundNumber;       // 1, 2, 3...

    private LocalDateTime scheduledAt;

    private String mode;               // "Online", "In-person"

    @Enumerated(EnumType.STRING)
    private InterviewResult result;

    private String feedback;
}