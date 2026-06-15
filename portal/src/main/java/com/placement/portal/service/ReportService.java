// service/ReportService.java
package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.model.*;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final InternshipPostingRepository internshipPostingRepository;
    private final InterviewRepository interviewRepository;

    // 1. Overall Placement Summary
    public PlacementSummaryResponse getPlacementSummary() {
        long totalStudents     = studentRepository.count();
        long totalPlaced       = applicationRepository.countByStatus(ApplicationStatus.SELECTED);
        long totalApplications = applicationRepository.count();
        long totalCompanies    = companyRepository.count();
        long totalJobs         = jobPostingRepository.count();
        long totalInternships  = internshipPostingRepository.count();

        double placementPercentage = totalStudents == 0 ? 0
                : Math.round((totalPlaced * 100.0 / totalStudents) * 100.0) / 100.0;

        Double avgCtc     = applicationRepository.findAverageCtcOfSelected();
        Double avgStipend = applicationRepository.findAverageStipendOfSelected();

        return PlacementSummaryResponse.builder()
                .totalStudents(totalStudents)
                .totalPlaced(totalPlaced)
                .totalUnplaced(totalStudents - totalPlaced)
                .totalApplications(totalApplications)
                .totalCompanies(totalCompanies)
                .totalJobPostings(totalJobs)
                .totalInternshipPostings(totalInternships)
                .placementPercentage(placementPercentage)
                .averageCtc(avgCtc != null ? Math.round(avgCtc * 100.0) / 100.0 : null)
                .averageStipend(avgStipend != null ? Math.round(avgStipend * 100.0) / 100.0 : null)
                .build();
    }

    // 2. Branch-wise Placement Report
    public List<BranchWiseReportResponse> getBranchWiseReport() {
        List<String> branches = studentRepository.findDistinctBranches();

        return branches.stream().map(branch -> {
            List<Student> students = studentRepository.findByBranch(branch);
            long total = students.size();

            // Count placed students in this branch
            long placed = students.stream()
                    .filter(s -> applicationRepository
                            .findByStudentId(s.getId())
                            .stream()
                            .anyMatch(a -> a.getStatus() == ApplicationStatus.SELECTED))
                    .count();

            Double avgCgpa = studentRepository.findAverageCgpaByBranch(branch);
            double percentage = total == 0 ? 0
                    : Math.round((placed * 100.0 / total) * 100.0) / 100.0;

            return BranchWiseReportResponse.builder()
                    .branch(branch)
                    .totalStudents(total)
                    .placed(placed)
                    .unplaced(total - placed)
                    .placementPercentage(percentage)
                    .averageCgpa(avgCgpa != null ? Math.round(avgCgpa * 100.0) / 100.0 : null)
                    .build();
        }).collect(Collectors.toList());
    }

    // 3. Company-wise Placement Report
    public List<CompanyWiseReportResponse> getCompanyWiseReport() {
        Map<Long, CompanyWiseReportResponse> reportMap = new HashMap<>();

        // Process job application stats
        applicationRepository.findJobApplicationStatsByCompany()
                .forEach(row -> {
                    Long companyId   = (Long) row[0];
                    String companyName = (String) row[1];
                    long total       = ((Number) row[2]).longValue();
                    long shortlisted = ((Number) row[3]).longValue();
                    long selected    = ((Number) row[4]).longValue();
                    long rejected    = ((Number) row[5]).longValue();
                    Double avgCtc    = row[6] != null ? ((Number) row[6]).doubleValue() : null;

                    reportMap.put(companyId, CompanyWiseReportResponse.builder()
                            .companyId(companyId)
                            .companyName(companyName)
                            .totalApplications(total)
                            .shortlisted(shortlisted)
                            .selected(selected)
                            .rejected(rejected)
                            .averageCtc(avgCtc)
                            .build());
                });

        // Merge internship stats into same company entry
        applicationRepository.findInternshipApplicationStatsByCompany()
                .forEach(row -> {
                    Long companyId   = (Long) row[0];
                    String companyName = (String) row[1];
                    long total       = ((Number) row[2]).longValue();
                    long shortlisted = ((Number) row[3]).longValue();
                    long selected    = ((Number) row[4]).longValue();
                    long rejected    = ((Number) row[5]).longValue();
                    Double avgStipend = row[6] != null ? ((Number) row[6]).doubleValue() : null;

                    reportMap.merge(companyId,
                            CompanyWiseReportResponse.builder()
                                    .companyId(companyId)
                                    .companyName(companyName)
                                    .totalApplications(total)
                                    .shortlisted(shortlisted)
                                    .selected(selected)
                                    .rejected(rejected)
                                    .averageStipend(avgStipend)
                                    .build(),
                            (existing, incoming) -> {
                                existing.setTotalApplications(
                                        existing.getTotalApplications() + incoming.getTotalApplications());
                                existing.setShortlisted(
                                        existing.getShortlisted() + incoming.getShortlisted());
                                existing.setSelected(
                                        existing.getSelected() + incoming.getSelected());
                                existing.setRejected(
                                        existing.getRejected() + incoming.getRejected());
                                existing.setAverageStipend(incoming.getAverageStipend());
                                return existing;
                            });
                });

        return new ArrayList<>(reportMap.values());
    }

    // 4. Student Placement Status Report
    public List<StudentPlacementReportResponse> getStudentPlacementReport() {
        return studentRepository.findAll().stream().map(student -> {
            List<Application> apps = applicationRepository.findByStudentId(student.getId());

            // Find selected application if any
            Optional<Application> selected = apps.stream()
                    .filter(a -> a.getStatus() == ApplicationStatus.SELECTED)
                    .findFirst();

            ApplicationStatus overallStatus = selected.isPresent()
                    ? ApplicationStatus.SELECTED
                    : apps.stream()
                      .map(Application::getStatus)
                      .max(Comparator.comparingInt(Enum::ordinal))
                      .orElse(null);

            String placedAt    = null;
            String placedRole  = null;
            String placementType = null;
            Double ctcOrStipend  = null;

            if (selected.isPresent()) {
                Application a = selected.get();
                if (a.getJobPosting() != null) {
                    placedAt       = a.getJobPosting().getCompany().getName();
                    placedRole     = a.getJobPosting().getTitle();
                    placementType  = "JOB";
                    ctcOrStipend   = a.getJobPosting().getCtc();
                } else if (a.getInternshipPosting() != null) {
                    placedAt       = a.getInternshipPosting().getCompany().getName();
                    placedRole     = a.getInternshipPosting().getTitle();
                    placementType  = "INTERNSHIP";
                    ctcOrStipend   = a.getInternshipPosting().getStipend();
                }
            }

            return StudentPlacementReportResponse.builder()
                    .studentId(student.getId())
                    .studentName(student.getName())
                    .branch(student.getBranch())
                    .cgpa(student.getCgpa())
                    .applicationStatus(overallStatus)
                    .placedAt(placedAt)
                    .placedRole(placedRole)
                    .placementType(placementType)
                    .ctcOrStipend(ctcOrStipend)
                    .build();
        }).collect(Collectors.toList());
    }

    // 5. Interview Success Rate Report
    public List<InterviewReportResponse> getInterviewReport() {
        List<InterviewReportResponse> reports = new ArrayList<>();

        // Job posting interview stats
        interviewRepository.findInterviewStatsByJobPosting()
                .forEach(row -> {
                    long total   = ((Number) row[3]).longValue();
                    long passed  = ((Number) row[4]).longValue();
                    long failed  = ((Number) row[5]).longValue();
                    long pending = ((Number) row[6]).longValue();
                    double passRate = total == 0 ? 0
                            : Math.round((passed * 100.0 / total) * 100.0) / 100.0;

                    reports.add(InterviewReportResponse.builder()
                            .companyId((Long) row[0])
                            .companyName((String) row[1])
                            .postingTitle((String) row[2])
                            .totalInterviews(total)
                            .passed(passed)
                            .failed(failed)
                            .pending(pending)
                            .passRate(passRate)
                            .build());
                });

        // Internship posting interview stats
        interviewRepository.findInterviewStatsByInternshipPosting()
                .forEach(row -> {
                    long total   = ((Number) row[3]).longValue();
                    long passed  = ((Number) row[4]).longValue();
                    long failed  = ((Number) row[5]).longValue();
                    long pending = ((Number) row[6]).longValue();
                    double passRate = total == 0 ? 0
                            : Math.round((passed * 100.0 / total) * 100.0) / 100.0;

                    reports.add(InterviewReportResponse.builder()
                            .companyId((Long) row[0])
                            .companyName((String) row[1])
                            .postingTitle((String) row[2])
                            .totalInterviews(total)
                            .passed(passed)
                            .failed(failed)
                            .pending(pending)
                            .passRate(passRate)
                            .build());
                });

        return reports;
    }
}