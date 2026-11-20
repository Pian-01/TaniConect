
package com.taniconnect.backend.repository;

import com.taniconnect.backend.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {

    List<PostMedia> findByPostIdOrderByDisplayOrderAsc(Long postId);

    List<PostMedia> findByPostIdInOrderByDisplayOrderAsc(List<Long> postIds);

    long countByPostId(Long postId);
}
