// service/InternshipPostingService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.exception.ResourceNotFoundException;
import com.placement.portal.model.*;
import com.placement.portal.repository.InternshipPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipPostingService {
    private final StudentService studentService;
    private final InternshipPostingRepository internshipPostingRepository;
    private final CompanyService companyService;

    public InternshipPostingResponse create(InternshipPostingRequest request) {
        Company company = companyService.findById(request.getCompanyId());

        InternshipPosting posting = InternshipPosting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredSkills(request.getRequiredSkills())
                .stipend(request.getStipend())
                .durationMonths(request.getDurationMonths())
                .mode(request.getMode())
                .status(PostingStatus.OPEN)
                .startDate(request.getStartDate())
                .company(company)
                .eligibleBranches(request.getEligibleBranches())
                .minCgpa(request.getMinCgpa())
                .build();

        return toResponse(internshipPostingRepository.save(posting));
    }

    public List<InternshipPostingResponse> getAll() {
        return internshipPostingRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InternshipPostingResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public List<InternshipPostingResponse> getByCompany(Long companyId) {
        return internshipPostingRepository.findByCompanyId(companyId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InternshipPostingResponse update(Long id, InternshipPostingRequest request) {
        InternshipPosting posting = findById(id);
        posting.setTitle(request.getTitle());
        posting.setDescription(request.getDescription());
        posting.setRequiredSkills(request.getRequiredSkills());
        posting.setStipend(request.getStipend());
        posting.setDurationMonths(request.getDurationMonths());
        posting.setMode(request.getMode());
        posting.setStartDate(request.getStartDate());
        posting.setEligibleBranches(request.getEligibleBranches());
        posting.setMinCgpa(request.getMinCgpa());
        return toResponse(internshipPostingRepository.save(posting));
    }

    public void delete(Long id) {
        internshipPostingRepository.delete(findById(id));
    }

    public InternshipPosting findById(Long id) {
        return internshipPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship posting not found with id: " + id));
    }

    private InternshipPostingResponse toResponse(InternshipPosting i) {
        return InternshipPostingResponse.builder()
                .id(i.getId())
                .title(i.getTitle())
                .description(i.getDescription())
                .requiredSkills(i.getRequiredSkills())
                .stipend(i.getStipend())
                .durationMonths(i.getDurationMonths())
                .mode(i.getMode())
                .startDate(i.getStartDate())
                .status(i.getStatus())
                .companyId(i.getCompany().getId())
                .companyName(i.getCompany().getName())
                .createdAt(i.getCreatedAt())
                .eligibleBranches(i.getEligibleBranches())
                .minCgpa(i.getMinCgpa())
                .build();
    }

    public List<InternshipPostingResponse> getEligibleInternships(Long studentId) {
        Student student = studentService.findById(studentId);
        return internshipPostingRepository
                .findEligibleInternships(student.getBranch(), student.getCgpa())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
}