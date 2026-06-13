// dto/StudentRequest.java
package com.placement.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StudentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String branch;

    @DecimalMin(value = "0.0") @DecimalMax(value = "10.0")
    private Double cgpa;

    private String skills;
    private String resumeUrl;
}