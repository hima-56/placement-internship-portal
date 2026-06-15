package com.placement.portal.repository;

import com.placement.portal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    // Distinct branches
    @Query("SELECT DISTINCT s.branch FROM Student s WHERE s.branch IS NOT NULL")
    List<String> findDistinctBranches();

    // Students by branch
    List<Student> findByBranch(String branch);

    // Average CGPA by branch
    @Query("SELECT AVG(s.cgpa) FROM Student s WHERE s.branch = :branch")
    Double findAverageCgpaByBranch(@Param("branch") String branch);
}