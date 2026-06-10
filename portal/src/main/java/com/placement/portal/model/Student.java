// model/Student.java
package com.placement.portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Student extends BaseEntity {

    @NotBlank
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String branch;       // e.g. "CSE", "ECE", "MECH"

    @DecimalMin("0.0") @DecimalMax("10.0")
    private Double cgpa;

    private String skills;       // comma-separated: "Java, Spring, MySQL"

    private String resumeUrl;
}