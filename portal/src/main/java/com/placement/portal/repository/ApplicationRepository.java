// repository/ApplicationRepository.java
package com.placement.portal.repository;
import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByJobPostingId(Long jobPostingId);
    List<Application> findByInternshipPostingId(Long internshipPostingId);
    List<Application> findByStatus(ApplicationStatus status);

    long countByStatus(ApplicationStatus status);

    // All selected applications (for student placement report)
    List<Application> findByStatusOrderByCreatedAtDesc(ApplicationStatus status);

    // Average CTC of selected job applications
    @Query("SELECT AVG(a.jobPosting.ctc) FROM Application a " +
            "WHERE a.status = 'SELECTED' AND a.jobPosting IS NOT NULL")
    Double findAverageCtcOfSelected();

    // Average stipend of selected internship applications
    @Query("SELECT AVG(a.internshipPosting.stipend) FROM Application a " +
            "WHERE a.status = 'SELECTED' AND a.internshipPosting IS NOT NULL")
    Double findAverageStipendOfSelected();

    // Applications grouped by company (job postings)
    @Query("SELECT a.jobPosting.company.id, a.jobPosting.company.name, " +
            "COUNT(a), " +
            "SUM(CASE WHEN a.status = 'SHORTLISTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'SELECTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'REJECTED' THEN 1 ELSE 0 END), " +
            "AVG(a.jobPosting.ctc) " +
            "FROM Application a WHERE a.jobPosting IS NOT NULL " +
            "GROUP BY a.jobPosting.company.id, a.jobPosting.company.name")
    List<Object[]> findJobApplicationStatsByCompany();

    // Applications grouped by company (internship postings)
    @Query("SELECT a.internshipPosting.company.id, a.internshipPosting.company.name, " +
            "COUNT(a), " +
            "SUM(CASE WHEN a.status = 'SHORTLISTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'SELECTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'REJECTED' THEN 1 ELSE 0 END), " +
            "AVG(a.internshipPosting.stipend) " +
            "FROM Application a WHERE a.internshipPosting IS NOT NULL " +
            "GROUP BY a.internshipPosting.company.id, a.internshipPosting.company.name")
    List<Object[]> findInternshipApplicationStatsByCompany();
}