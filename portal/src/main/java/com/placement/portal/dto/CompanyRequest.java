// dto/CompanyRequest.java
package com.placement.portal.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String domain;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String location;
    private String description;
}
