// service/ExcelExportService.java
package com.placement.portal.service;

import com.placement.portal.config.ExcelHelper;
import com.placement.portal.dto.*;
import com.placement.portal.model.*;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final ReportService reportService;
    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;

    // ─── Master Export: all sheets in one workbook ───────────────────────────
    public ByteArrayInputStream exportFullReport() throws IOException {
        Workbook workbook = ExcelHelper.createWorkbook();

        buildSummarySheet(workbook);
        buildStudentSheet(workbook);
        buildBranchWiseSheet(workbook);
        buildCompanyWiseSheet(workbook);
        buildInterviewSheet(workbook);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    // ─── Sheet 1: Placement Summary ──────────────────────────────────────────
    private void buildSummarySheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Placement Summary");
        CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
        CellStyle dataStyle   = ExcelHelper.createDataStyle(workbook);

        PlacementSummaryResponse summary = reportService.getPlacementSummary();

        // Title row
        Row title = sheet.createRow(0);
        Cell titleCell = title.createCell(0);
        titleCell.setCellValue("PLACEMENT & INTERNSHIP PORTAL — SUMMARY REPORT");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // Empty row
        sheet.createRow(1);

        // Data rows
        String[][] data = {
                {"Total Students",            String.valueOf(summary.getTotalStudents())},
                {"Total Placed",              String.valueOf(summary.getTotalPlaced())},
                {"Total Unplaced",            String.valueOf(summary.getTotalUnplaced())},
                {"Placement Percentage",      summary.getPlacementPercentage() + "%"},
                {"Total Applications",        String.valueOf(summary.getTotalApplications())},
                {"Total Companies",           String.valueOf(summary.getTotalCompanies())},
                {"Total Job Postings",        String.valueOf(summary.getTotalJobPostings())},
                {"Total Internship Postings", String.valueOf(summary.getTotalInternshipPostings())},
                {"Average CTC (LPA)",         summary.getAverageCtc() != null
                        ? String.valueOf(summary.getAverageCtc()) : "N/A"},
                {"Average Stipend (Monthly)", summary.getAverageStipend() != null
                        ? String.valueOf(summary.getAverageStipend()) : "N/A"},
        };

        int rowNum = 2;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            Cell label = row.createCell(0);
            label.setCellValue(rowData[0]);
            label.setCellStyle(headerStyle);
            Cell value = row.createCell(1);
            value.setCellValue(rowData[1]);
            value.setCellStyle(dataStyle);
        }

        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 5000);
    }

    // ─── Sheet 2: Student Placement Status ───────────────────────────────────
    private void buildStudentSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Student Report");
        CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
        CellStyle dataStyle   = ExcelHelper.createDataStyle(workbook);

        ExcelHelper.createHeaderRow(sheet, headerStyle,
                "Student ID", "Name", "Branch", "CGPA",
                "Status", "Placed At", "Role", "Type", "CTC / Stipend");

        List<StudentPlacementReportResponse> students = reportService.getStudentPlacementReport();

        int rowNum = 1;
        for (StudentPlacementReportResponse s : students) {
            Row row = sheet.createRow(rowNum++);

            createCell(row, 0, String.valueOf(s.getStudentId()), dataStyle);
            createCell(row, 1, s.getStudentName(), dataStyle);
            createCell(row, 2, s.getBranch(), dataStyle);
            createCell(row, 3, s.getCgpa() != null ? String.valueOf(s.getCgpa()) : "N/A", dataStyle);
            createCell(row, 4, s.getApplicationStatus() != null
                    ? s.getApplicationStatus().name() : "NOT APPLIED", dataStyle);
            createCell(row, 5, s.getPlacedAt() != null ? s.getPlacedAt() : "-", dataStyle);
            createCell(row, 6, s.getPlacedRole() != null ? s.getPlacedRole() : "-", dataStyle);
            createCell(row, 7, s.getPlacementType() != null ? s.getPlacementType() : "-", dataStyle);
            createCell(row, 8, s.getCtcOrStipend() != null
                    ? String.valueOf(s.getCtcOrStipend()) : "-", dataStyle);
        }

        ExcelHelper.autoSizeColumns(sheet, 9);
    }

    // ─── Sheet 3: Branch-wise Report ─────────────────────────────────────────
    private void buildBranchWiseSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Branch-wise Report");
        CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
        CellStyle dataStyle   = ExcelHelper.createDataStyle(workbook);

        ExcelHelper.createHeaderRow(sheet, headerStyle,
                "Branch", "Total Students", "Placed", "Unplaced",
                "Placement %", "Average CGPA");

        List<BranchWiseReportResponse> branches = reportService.getBranchWiseReport();

        int rowNum = 1;
        for (BranchWiseReportResponse b : branches) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, b.getBranch(), dataStyle);
            createCell(row, 1, String.valueOf(b.getTotalStudents()), dataStyle);
            createCell(row, 2, String.valueOf(b.getPlaced()), dataStyle);
            createCell(row, 3, String.valueOf(b.getUnplaced()), dataStyle);
            createCell(row, 4, b.getPlacementPercentage() + "%", dataStyle);
            createCell(row, 5, b.getAverageCgpa() != null
                    ? String.valueOf(b.getAverageCgpa()) : "N/A", dataStyle);
        }

        ExcelHelper.autoSizeColumns(sheet, 6);
    }

    // ─── Sheet 4: Company-wise Report ────────────────────────────────────────
    private void buildCompanyWiseSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Company-wise Report");
        CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
        CellStyle dataStyle   = ExcelHelper.createDataStyle(workbook);

        ExcelHelper.createHeaderRow(sheet, headerStyle,
                "Company ID", "Company Name", "Total Applications",
                "Shortlisted", "Selected", "Rejected",
                "Avg CTC (LPA)", "Avg Stipend (Monthly)");

        List<CompanyWiseReportResponse> companies = reportService.getCompanyWiseReport();

        int rowNum = 1;
        for (CompanyWiseReportResponse c : companies) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, String.valueOf(c.getCompanyId()), dataStyle);
            createCell(row, 1, c.getCompanyName(), dataStyle);
            createCell(row, 2, String.valueOf(c.getTotalApplications()), dataStyle);
            createCell(row, 3, String.valueOf(c.getShortlisted()), dataStyle);
            createCell(row, 4, String.valueOf(c.getSelected()), dataStyle);
            createCell(row, 5, String.valueOf(c.getRejected()), dataStyle);
            createCell(row, 6, c.getAverageCtc() != null
                    ? String.valueOf(c.getAverageCtc()) : "N/A", dataStyle);
            createCell(row, 7, c.getAverageStipend() != null
                    ? String.valueOf(c.getAverageStipend()) : "N/A", dataStyle);
        }

        ExcelHelper.autoSizeColumns(sheet, 8);
    }

    // ─── Sheet 5: Interview Report ────────────────────────────────────────────
    private void buildInterviewSheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Interview Report");
        CellStyle headerStyle = ExcelHelper.createHeaderStyle(workbook);
        CellStyle dataStyle   = ExcelHelper.createDataStyle(workbook);

        ExcelHelper.createHeaderRow(sheet, headerStyle,
                "Company", "Posting Title", "Total Interviews",
                "Passed", "Failed", "Pending", "Pass Rate %");

        List<InterviewReportResponse> interviews = reportService.getInterviewReport();

        int rowNum = 1;
        for (InterviewReportResponse i : interviews) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, i.getCompanyName(), dataStyle);
            createCell(row, 1, i.getPostingTitle(), dataStyle);
            createCell(row, 2, String.valueOf(i.getTotalInterviews()), dataStyle);
            createCell(row, 3, String.valueOf(i.getPassed()), dataStyle);
            createCell(row, 4, String.valueOf(i.getFailed()), dataStyle);
            createCell(row, 5, String.valueOf(i.getPending()), dataStyle);
            createCell(row, 6, i.getPassRate() + "%", dataStyle);
        }

        ExcelHelper.autoSizeColumns(sheet, 7);
    }

    // ─── Utility ─────────────────────────────────────────────────────────────
    private void createCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }
}