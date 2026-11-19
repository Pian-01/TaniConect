package com.taniconnect.backend.repository;

import com.taniconnect.backend.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    List<PostLike> findByPostId(Long postId);

    long countByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);
}