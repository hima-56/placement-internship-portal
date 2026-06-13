package com.placement.portal.repository;

import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByCompanyId(Long companyId);
    List<JobPosting> findByStatus(PostingStatus status);

    // Find open jobs where student's branch is listed AND cgpa meets minimum
    @Query("SELECT j FROM JobPosting j WHERE j.status = 'OPEN' " +
            "AND (j.eligibleBranches IS NULL OR j.eligibleBranches LIKE CONCAT('%', :branch, '%')) " +
            "AND (j.minCgpa IS NULL OR j.minCgpa <= :cgpa)")
    List<JobPosting> findEligibleJobs(@Param("branch") String branch,
                                      @Param("cgpa") Double cgpa);

}