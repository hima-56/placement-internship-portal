// service/InterviewService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.exception.*;
import com.placement.portal.model.*;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    // Company schedules round 1 (or next round if previous passed)
    public InterviewResponse scheduleInterview(InterviewRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found: " + request.getApplicationId()));

        // Check previous round result before allowing next round
        interviewRepository.findLatestRoundByApplicationId(request.getApplicationId())
                .ifPresent(latest -> {
                    if (latest.getResult() == null || latest.getResult() == InterviewResult.PENDING) {
                        throw new IllegalStateException(
                                "Round " + latest.getRoundNumber() + " result is still pending. " +
                                        "Update the result before scheduling the next round.");
                    }
                    if (latest.getResult() == InterviewResult.FAILED) {
                        throw new IllegalStateException(
                                "Student failed round " + latest.getRoundNumber() +
                                        ". Cannot schedule further rounds.");
                    }
                });

        // Determine round number automatically
        int nextRound = interviewRepository
                .findLatestRoundByApplicationId(request.getApplicationId())
                .map(i -> i.getRoundNumber() + 1)
                .orElse(1);

        Interview interview = Interview.builder()
                .application(application)
                .roundNumber(nextRound)
                .scheduledAt(request.getScheduledAt())
                .mode(request.getMode())
                .result(InterviewResult.PENDING)
                .feedback(request.getFeedback())
                .build();

        // Update application status
        application.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);

        return toResponse(interviewRepository.save(interview));
    }

    // Company updates round result (PASSED or FAILED)
    public InterviewResponse updateResult(Long interviewId, InterviewResult result, String feedback) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + interviewId));

        interview.setResult(result);
        interview.setFeedback(feedback);

        // If final result — update application status
        Application application = interview.getApplication();
        if (result == InterviewResult.FAILED) {
            application.setStatus(ApplicationStatus.REJECTED);
            applicationRepository.save(application);
        } else if (result == InterviewResult.PASSED) {
            // Only mark SELECTED if explicitly confirmed (can also be done via separate endpoint)
            application.setStatus(ApplicationStatus.SHORTLISTED);
            applicationRepository.save(application);
        }

        return toResponse(interviewRepository.save(interview));
    }

    // Student views all their interviews
    public List<InterviewResponse> getInterviewsForStudent(Long studentId) {
        return interviewRepository.findByStudentId(studentId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Get all interviews for an application (full round history)
    public List<InterviewResponse> getByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private InterviewResponse toResponse(Interview i) {
        Application app = i.getApplication();
        String postingTitle = app.getJobPosting() != null
                ? app.getJobPosting().getTitle()
                : app.getInternshipPosting().getTitle();

        String companyName = app.getJobPosting() != null
                ? app.getJobPosting().getCompany().getName()
                : app.getInternshipPosting().getCompany().getName();

        return InterviewResponse.builder()
                .id(i.getId())
                .applicationId(app.getId())
                .studentId(app.getStudent().getId())
                .studentName(app.getStudent().getName())
                .postingTitle(postingTitle)
                .companyName(companyName)
                .roundNumber(i.getRoundNumber())
                .scheduledAt(i.getScheduledAt())
                .mode(i.getMode())
                .result(i.getResult())
                .feedback(i.getFeedback())
                .build();
    }
}