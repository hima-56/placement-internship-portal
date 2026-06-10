// dto/CompanyResponse.java
package com.placement.portal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompanyResponse {
    private Long id;
    private String name;
    private String domain;
    private String email;
    private String location;
    private String description;
    private LocalDateTime createdAt;
}
