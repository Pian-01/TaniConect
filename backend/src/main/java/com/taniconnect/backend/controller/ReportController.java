
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.CreateReportRequest;
import com.taniconnect.backend.dto.ReportResponse;
import com.taniconnect.backend.dto.UpdateReportStatusRequest;
import com.taniconnect.backend.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponse createReport(
            Authentication authentication,
            @Valid @RequestBody CreateReportRequest request) {
        return reportService.createReport(authentication.getName(), request);
    }

    @GetMapping("/me")
    public List<ReportResponse> getMyReports(Authentication authentication) {
        return reportService.getMyReports(authentication.getName());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportResponse> getAllReports() {
        return reportService.getAllReports();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ReportResponse updateReportStatus(
        @PathVariable Long id,
        Authentication authentication,
        @Valid @RequestBody UpdateReportStatusRequest request
    ) {
        return reportService.updateReportStatus(
            id,
            authentication.getName(),
            request.status()
        );
    }

    
}
