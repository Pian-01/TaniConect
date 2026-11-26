package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.CreateReportRequest;
import com.taniconnect.backend.dto.ReportResponse;
import com.taniconnect.backend.entity.*;
import com.taniconnect.backend.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;

    public ReportService(
            ReportRepository reportRepository,
            UserRepository userRepository,
            PostRepository postRepository,
            PostCommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ReportResponse createReport(String username, CreateReportRequest request) {
        User reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Report report = new Report();
        report.setReporter(reporter);
        report.setTargetType(request.targetType());
        report.setReason(request.reason());
        report.setDescription(request.description());

        switch (request.targetType()) {
            case "POST" -> {
                Post post = postRepository.findById(request.targetId())
                        .orElseThrow(() -> new RuntimeException("Post tidak ditemukan"));
                report.setPost(post);
            }
            case "COMMENT" -> {
                PostComment comment = commentRepository.findById(request.targetId())
                        .orElseThrow(() -> new RuntimeException("Komentar tidak ditemukan"));
                report.setComment(comment);
            }
            case "ACCOUNT" -> {
                User reportedUser = userRepository.findById(request.targetId())
                        .orElseThrow(() -> new RuntimeException("User yang dilaporkan tidak ditemukan"));
                report.setReportedUser(reportedUser);
            }
        }

        return toResponse(reportRepository.save(report));
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getMyReports(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        return reportRepository.findByReporterIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ReportResponse updateReportStatus(
        Long reportId,
        String username,
        String status
    ) {
    Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Laporan tidak ditemukan"));

    User admin = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Admin tidak ditemukan"));

        report.setStatus(status);
        report.setReviewedBy(admin);
        report.setReviewedAt(LocalDateTime.now());

        return toResponse(reportRepository.save(report));
    }   

    private ReportResponse toResponse(Report report) {
        Long targetId = switch (report.getTargetType()) {
            case "POST" -> report.getPost().getId();
            case "COMMENT" -> report.getComment().getId();
            case "ACCOUNT" -> report.getReportedUser().getId();
            default -> null;
        };

        return new ReportResponse(
                report.getId(),
                report.getReporter().getId(),
                report.getTargetType(),
                targetId,
                report.getReason(),
                report.getDescription(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}
