package com.taniconnect.backend.repository;

import com.taniconnect.backend.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository
        extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    Optional<PostComment> findByIdAndUserId(Long commentId, Long userId);

    List<PostComment> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByPostId(Long postId);

    Page<PostComment> findByPostIdAndParentCommentIsNullOrderByCreatedAtAsc(
        Long postId,
        Pageable pageable
    );
}