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
}