package com.placement.portal.repository;

import com.placement.portal.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationId(Long applicationId);

    // All interviews for a student across all applications
    @Query("SELECT i FROM Interview i WHERE i.application.student.id = :studentId " +
            "ORDER BY i.scheduledAt ASC")
    List<Interview> findByStudentId(@Param("studentId") Long studentId);

    // Latest round for an application (used before scheduling next round)
    @Query("SELECT i FROM Interview i WHERE i.application.id = :applicationId " +
            "ORDER BY i.roundNumber DESC LIMIT 1")
    Optional<Interview> findLatestRoundByApplicationId(@Param("applicationId") Long applicationId);
    // Count by result
    long countByResult(InterviewResult result);

    // Interview stats per job posting
    @Query("SELECT i.application.jobPosting.company.id, " +
            "i.application.jobPosting.company.name, " +
            "i.application.jobPosting.title, " +
            "COUNT(i), " +
            "SUM(CASE WHEN i.result = 'PASSED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN i.result = 'FAILED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN i.result = 'PENDING' THEN 1 ELSE 0 END) " +
            "FROM Interview i WHERE i.application.jobPosting IS NOT NULL " +
            "GROUP BY i.application.jobPosting.company.id, " +
            "i.application.jobPosting.company.name, " +
            "i.application.jobPosting.title")
    List<Object[]> findInterviewStatsByJobPosting();

    // Interview stats per internship posting
    @Query("SELECT i.application.internshipPosting.company.id, " +
            "i.application.internshipPosting.company.name, " +
            "i.application.internshipPosting.title, " +
            "COUNT(i), " +
            "SUM(CASE WHEN i.result = 'PASSED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN i.result = 'FAILED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN i.result = 'PENDING' THEN 1 ELSE 0 END) " +
            "FROM Interview i WHERE i.application.internshipPosting IS NOT NULL " +
            "GROUP BY i.application.internshipPosting.company.id, " +
            "i.application.internshipPosting.company.name, " +
            "i.application.internshipPosting.title")
    List<Object[]> findInterviewStatsByInternshipPosting();


}