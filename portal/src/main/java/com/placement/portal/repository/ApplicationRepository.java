// repository/ApplicationRepository.java
package com.placement.portal.repository;
import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByJobPostingId(Long jobPostingId);
    List<Application> findByInternshipPostingId(Long internshipPostingId);
    List<Application> findByStatus(ApplicationStatus status);
}