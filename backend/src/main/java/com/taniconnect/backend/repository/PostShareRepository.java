
package com.taniconnect.backend.repository;

import com.taniconnect.backend.entity.PostShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostShareRepository extends JpaRepository<PostShare, Long> {

    List<PostShare> findByPostIdOrderByCreatedAtDesc(Long postId);

    List<PostShare> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
