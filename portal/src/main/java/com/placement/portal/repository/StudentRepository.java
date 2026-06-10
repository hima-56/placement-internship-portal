// repository/StudentRepository.java
package com.placement.portal.repository;
import com.placement.portal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
}