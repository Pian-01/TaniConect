
package com.taniconnect.backend.repository;

import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.Follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Feed Latest
    List<Post> findAllByOrderByCreatedAtDesc();

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Post berdasarkan user
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Feed Following
    @Query("""
        SELECT p FROM Post p
        WHERE p.user.id IN (
            SELECT f.following.id
            FROM Follow f
            WHERE f.follower.id = :userId
        )
        ORDER BY p.createdAt DESC
    """)
    Page<Post> findFollowingPosts(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
