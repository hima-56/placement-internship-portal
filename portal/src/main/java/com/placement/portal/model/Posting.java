// model/Posting.java  (abstract base — not a table itself)
package com.placement.portal.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@MappedSuperclass
public abstract class Posting extends BaseEntity {

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String requiredSkills;

    @Enumerated(EnumType.STRING)
    private PostingStatus status = PostingStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}