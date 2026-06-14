// service/ApplicationService.java
package com.placement.portal.service;
import com.placement.portal.config.NotificationManager;
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
        if (request.getJobPostingId() == null && request.getInternshipPostingId() == null)
            throw new IllegalArgumentException("Either jobPostingId or internshipPostingId is required");

        if (request.getJobPostingId() != null && request.getInternshipPostingId() != null)
            throw new IllegalArgumentException("Provide only one of jobPostingId or internshipPostingId");

        Student student = studentService.findById(request.getStudentId());
        Application application = new Application();
        application.setStudent(student);

        if (request.getJobPostingId() != null) {
            JobPosting job = jobPostingService.findById(request.getJobPostingId());

            // Eligibility guard
            boolean cgpaMet = job.getMinCgpa() == null
                    || (student.getCgpa() != null && student.getCgpa() >= job.getMinCgpa());

            if (!cgpaMet && !job.isManualReviewEnabled())
                throw new IllegalStateException(
                        "Application rejected: Your CGPA (" + student.getCgpa()
                                + ") is below the required cutoff of " + job.getMinCgpa()
                                + ". This posting does not allow manual review.");

            application.setJobPosting(job);

        } else {
            InternshipPosting internship =
                    internshipPostingService.findById(request.getInternshipPostingId());

            boolean cgpaMet = internship.getMinCgpa() == null
                    || (student.getCgpa() != null && student.getCgpa() >= internship.getMinCgpa());

            if (!cgpaMet && !internship.isManualReviewEnabled())
                throw new IllegalStateException(
                        "Application rejected: Your CGPA (" + student.getCgpa()
                                + ") is below the required cutoff of " + internship.getMinCgpa()
                                + ". This posting does not allow manual review.");

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



    // ... inside shortlist()
    public ApplicationResponse shortlist(Long applicationId) {
        Application application = findById(applicationId);

        if (application.getStatus() != ApplicationStatus.APPLIED)
            throw new IllegalStateException(
                    "Only applications with status APPLIED can be shortlisted. Current status: "
                            + application.getStatus());

        application.setStatus(ApplicationStatus.SHORTLISTED);
        Application saved = applicationRepository.save(application);

        NotificationManager.getInstance().notify("STUDENT",
                "You have been shortlisted for " +
                        (saved.getJobPosting() != null ? saved.getJobPosting().getTitle()
                                : saved.getInternshipPosting().getTitle()));

        return toResponse(saved);
    }
    // Bulk shortlist: company shortlists all applicants for a job who meet CGPA threshold
    public List<ApplicationResponse> bulkShortlistByJob(Long jobPostingId, Double minCgpa) {
        List<Application> applications = applicationRepository.findByJobPostingId(jobPostingId);

        List<Application> eligible = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.APPLIED)
                .filter(a -> a.getStudent().getCgpa() != null && a.getStudent().getCgpa() >= minCgpa)
                .peek(a -> a.setStatus(ApplicationStatus.SHORTLISTED))
                .collect(Collectors.toList());

        return applicationRepository.saveAll(eligible)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Reject an application
    public ApplicationResponse reject(Long applicationId) {
        Application application = findById(applicationId);
        application.setStatus(ApplicationStatus.REJECTED);
        return toResponse(applicationRepository.save(application));
    }

    // Final selection - mark student as SELECTED (placed)
    public ApplicationResponse markSelected(Long applicationId) {
        Application application = findById(applicationId);

        if (application.getStatus() != ApplicationStatus.SHORTLISTED)
            throw new IllegalStateException(
                    "Only SHORTLISTED applications can be marked SELECTED. Current status: "
                            + application.getStatus());

        application.setStatus(ApplicationStatus.SELECTED);
        NotificationManager.getInstance().notify("STUDENT",
                "Congratulations! You have been SELECTED for " +
                        (application.getJobPosting()!= null ? application.getJobPosting().getTitle()
                                : application.getInternshipPosting().getTitle()));
        return toResponse(applicationRepository.save(application));
    }


}