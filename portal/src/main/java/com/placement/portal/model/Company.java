// model/Company.java
package com.placement.portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String domain;       // e.g. "Product", "Service", "Startup"

    @Email
    @Column(unique = true)
    private String email;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;
}