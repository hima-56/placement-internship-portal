// dto/StudentResponse.java
package com.placement.portal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private String branch;
    private Double cgpa;
    private String skills;
    private String resumeUrl;
    private LocalDateTime createdAt;
}