// service/CompanyService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.exception.*;
import com.placement.portal.model.Company;
import com.placement.portal.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponse register(CompanyRequest request) {
        if (companyRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Company with email already exists: " + request.getEmail());

        Company company = Company.builder()
                .name(request.getName())
                .domain(request.getDomain())
                .email(request.getEmail())
                .location(request.getLocation())
                .description(request.getDescription())
                .build();

        return toResponse(companyRepository.save(company));
    }

    public List<CompanyResponse> getAll() {
        return companyRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CompanyResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = findById(id);
        company.setName(request.getName());
        company.setDomain(request.getDomain());
        company.setEmail(request.getEmail());
        company.setLocation(request.getLocation());
        company.setDescription(request.getDescription());
        return toResponse(companyRepository.save(company));
    }

    public void delete(Long id) {
        companyRepository.delete(findById(id));
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    private CompanyResponse toResponse(Company c) {
        return CompanyResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .domain(c.getDomain())
                .email(c.getEmail())
                .location(c.getLocation())
                .description(c.getDescription())
                .createdAt(c.getCreatedAt())
                .build();
    }
}