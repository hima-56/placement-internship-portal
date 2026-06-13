// service/JobPostingService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.exception.ResourceNotFoundException;
import com.placement.portal.model.*;
import com.placement.portal.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final StudentService studentService;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyService companyService;

    public JobPostingResponse create(JobPostingRequest request) {
        Company company = companyService.findById(request.getCompanyId());

        JobPosting posting = JobPosting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredSkills(request.getRequiredSkills())
                .ctc(request.getCtc())
                .jobType(request.getJobType())
                .location(request.getLocation())
                .deadline(request.getDeadline())
                .company(company)
                .status(PostingStatus.OPEN)
                .eligibleBranches(request.getEligibleBranches())
                .minCgpa(request.getMinCgpa())
                .build();

        return toResponse(jobPostingRepository.save(posting));
    }

    public List<JobPostingResponse> getAll() {
        return jobPostingRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public JobPostingResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public List<JobPostingResponse> getByCompany(Long companyId) {
        return jobPostingRepository.findByCompanyId(companyId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public JobPostingResponse update(Long id, JobPostingRequest request) {
        JobPosting posting = findById(id);
        posting.setTitle(request.getTitle());
        posting.setDescription(request.getDescription());
        posting.setRequiredSkills(request.getRequiredSkills());
        posting.setCtc(request.getCtc());
        posting.setJobType(request.getJobType());
        posting.setLocation(request.getLocation());
        posting.setDeadline(request.getDeadline());
        posting.setEligibleBranches(request.getEligibleBranches());
        posting.setMinCgpa(request.getMinCgpa());
        return toResponse(jobPostingRepository.save(posting));
    }

    public void delete(Long id) {
        jobPostingRepository.delete(findById(id));
    }

    public JobPosting findById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + id));
    }

    private JobPostingResponse toResponse(JobPosting j) {
        return JobPostingResponse.builder()
                .id(j.getId())
                .title(j.getTitle())
                .description(j.getDescription())
                .requiredSkills(j.getRequiredSkills())
                .ctc(j.getCtc())
                .jobType(j.getJobType())
                .location(j.getLocation())
                .deadline(j.getDeadline())
                .status(j.getStatus())
                .companyId(j.getCompany().getId())
                .companyName(j.getCompany().getName())
                .createdAt(j.getCreatedAt())
                .eligibleBranches(j.getEligibleBranches())
                .minCgpa(j.getMinCgpa())
                .build();
    }
    public List<JobPostingResponse> getEligibleJobs(Long studentId) {
        Student student = studentService.findById(studentId);
        return jobPostingRepository
                .findEligibleJobs(student.getBranch(), student.getCgpa())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
}