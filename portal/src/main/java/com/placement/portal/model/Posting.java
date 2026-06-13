// model/Posting.java  (abstract base — not a table itself)
package com.placement.portal.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public abstract class Posting extends BaseEntity {

    @NotBlank
    private String title ;
    @Column(columnDefinition = "TEXT")
    private String description;
    // model/Posting.java — add these two fields
    @Column(name = "eligible_branches")
    private String eligibleBranches;   // comma-separated: "CSE,ECE,IT"

    @Column(name = "min_cgpa")
    private Double minCgpa;            // e.g. 7.0
    private String requiredSkills;

    @Enumerated(EnumType.STRING)
    private PostingStatus status = PostingStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}