package com.placement.portal.repository;

import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByCompanyId(Long companyId);
    List<JobPosting> findByStatus(PostingStatus status);

    // Only filter by branch — CGPA logic handled in service
    @Query("SELECT j FROM JobPosting j WHERE j.status = 'OPEN' " +
            "AND (j.eligibleBranches IS NULL OR " +
            "j.eligibleBranches LIKE CONCAT('%', :branch, '%'))")
    List<JobPosting> findByBranch(@Param("branch") String branch);

}