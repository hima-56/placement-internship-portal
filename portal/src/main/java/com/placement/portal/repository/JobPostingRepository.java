// repository/JobPostingRepository.java
package com.placement.portal.repository;
import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByCompanyId(Long companyId);
    List<JobPosting> findByStatus(PostingStatus status);
}