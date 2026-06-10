// repository/CompanyRepository.java
package com.placement.portal.repository;
import com.placement.portal.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByEmail(String email);
}