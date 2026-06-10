// repository/InternshipPostingRepository.java
package com.placement.portal.repository;
import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface InternshipPostingRepository extends JpaRepository<InternshipPosting, Long> {
    List<InternshipPosting> findByCompanyId(Long companyId);
    List<InternshipPosting> findByStatus(PostingStatus status);
}