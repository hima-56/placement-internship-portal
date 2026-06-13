// service/ApplicationService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.exception.*;
import com.placement.portal.model.*;
import com.placement.portal.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentService studentService;
    private final JobPostingService jobPostingService;
    private final InternshipPostingService internshipPostingService;

    public ApplicationResponse apply(ApplicationRequest request) {
        // Validate: exactly one posting type must be provided
        if (request.getJobPostingId() == null && request.getInternshipPostingId() == null)
            throw new IllegalArgumentException("Either jobPostingId or internshipPostingId must be provided");

        if (request.getJobPostingId() != null && request.getInternshipPostingId() != null)
            throw new IllegalArgumentException("Provide only one of jobPostingId or internshipPostingId");

        Student student = studentService.findById(request.getStudentId());
        Application application = new Application();
        application.setStudent(student);

        if (request.getJobPostingId() != null) {
            JobPosting job = jobPostingService.findById(request.getJobPostingId());
            application.setJobPosting(job);
        } else {
            InternshipPosting internship = internshipPostingService.findById(request.getInternshipPostingId());
            application.setInternshipPosting(internship);
        }

        return toResponse(applicationRepository.save(application));
    }

    public List<ApplicationResponse> getAll() {
        return applicationRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ApplicationResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public List<ApplicationResponse> getByStudent(Long studentId) {
        return applicationRepository.findByStudentId(studentId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ApplicationResponse> getByJobPosting(Long jobPostingId) {
        return applicationRepository.findByJobPostingId(jobPostingId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ApplicationResponse> getByInternshipPosting(Long internshipPostingId) {
        return applicationRepository.findByInternshipPostingId(internshipPostingId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Application findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    private ApplicationResponse toResponse(Application a) {
        String postingTitle;
        String postingType;
        Long jobId = null;
        Long internshipId = null;

        if (a.getJobPosting() != null) {
            postingTitle = a.getJobPosting().getTitle();
            postingType = "JOB";
            jobId = a.getJobPosting().getId();
        } else {
            postingTitle = a.getInternshipPosting().getTitle();
            postingType = "INTERNSHIP";
            internshipId = a.getInternshipPosting().getId();
        }

        return ApplicationResponse.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getName())
                .jobPostingId(jobId)
                .internshipPostingId(internshipId)
                .postingTitle(postingTitle)
                .postingType(postingType)
                .status(a.getStatus())
                .appliedOn(a.getAppliedOn())
                .createdAt(a.getCreatedAt())
                .build();
    }
}