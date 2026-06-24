// service/JobPostingService.java
package com.placement.portal.service;

import com.placement.portal.config.PostingFactory;
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
    private final PostingFactory postingFactory;


    public JobPostingResponse create(JobPostingRequest request) {
        Company company = companyService.findById(request.getCompanyId());

        // ✅ Factory creates the right object type
        JobPosting posting = (JobPosting) postingFactory.createPosting(PostingFactory.TYPE_JOB);

        // Set fields manually after factory creates the instance
        posting.setTitle(request.getTitle());
        posting.setDescription(request.getDescription());
        posting.setRequiredSkills(request.getRequiredSkills());
        posting.setStatus(PostingStatus.OPEN);
        posting.setCompany(company);
        posting.setEligibleBranches(request.getEligibleBranches());
        posting.setMinCgpa(request.getMinCgpa());
        posting.setManualReviewEnabled(request.isManualReviewEnabled());
        posting.setCtc(request.getCtc());
        posting.setJobType(request.getJobType());
        posting.setLocation(request.getLocation());
        posting.setDeadline(request.getDeadline());

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
        posting.setManualReviewEnabled(request.isManualReviewEnabled());
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
                .manualReviewEnabled(j.isManualReviewEnabled())
                .build();
    }
    public List<JobOpportunityResponse> getOpportunitiesForStudent(Long studentId) {
        Student student = studentService.findById(studentId);

        return jobPostingRepository.findByBranch(student.getBranch())
                .stream()
                .map(job -> buildOpportunityResponse(job, student))
                .collect(Collectors.toList());
    }

    private JobOpportunityResponse buildOpportunityResponse(JobPosting job, Student student) {
        EligibilityStatus status;
        boolean canApply;
        String message;

        boolean branchMatch = job.getEligibleBranches() == null
                || job.getEligibleBranches().contains(student.getBranch());

        boolean cgpaMet = job.getMinCgpa() == null
                || (student.getCgpa() != null && student.getCgpa() >= job.getMinCgpa());

        if (!branchMatch) {
            status = EligibilityStatus.NOT_ELIGIBLE;
            canApply = false;
            message = "Your branch is not eligible for this posting.";
        } else if (!cgpaMet) {
            status = EligibilityStatus.CGPA_BELOW_CUTOFF;
            if (job.isManualReviewEnabled()) {
                canApply = true;
                message = "Your CGPA is below the cutoff (" + job.getMinCgpa()
                        + "). Manual review is enabled — you may still apply.";
            } else {
                canApply = false;
                message = "Your CGPA (" + student.getCgpa()
                        + ") is below the required cutoff of " + job.getMinCgpa() + ".";
            }
        } else {
            status = EligibilityStatus.ELIGIBLE;
            canApply = true;
            message = "You are eligible to apply.";
        }

        return JobOpportunityResponse.builder()
                .jobPosting(toResponse(job))
                .eligibilityStatus(status)
                .canApply(canApply)
                .eligibilityMessage(message)
                .build();
    }

}