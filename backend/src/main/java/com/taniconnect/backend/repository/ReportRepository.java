
package com.taniconnect.backend.repository;

import com.taniconnect.backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReporterIdOrderByCreatedAtDesc(Long reporterId);

    List<Report> findByStatusOrderByCreatedAtDesc(String status);

    boolean existsByReporterIdAndPostId(Long reporterId, Long postId);

    boolean existsByReporterIdAndCommentId(Long reporterId, Long commentId);
}
