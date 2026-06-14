// repository/InternshipPostingRepository.java
package com.placement.portal.repository;

import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InternshipPostingRepository extends JpaRepository<InternshipPosting, Long> {

    List<InternshipPosting> findByCompanyId(Long companyId);
    List<InternshipPosting> findByStatus(PostingStatus status);

    @Query("SELECT i FROM InternshipPosting i WHERE i.status = 'OPEN' " +
            "AND (i.eligibleBranches IS NULL OR " +
            "i.eligibleBranches LIKE CONCAT('%', :branch, '%'))")
    List<InternshipPosting> findByBranch(@Param("branch") String branch);
}