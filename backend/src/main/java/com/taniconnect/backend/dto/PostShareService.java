
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.PostShareResponse;
import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.PostShare;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.PostShareRepository;
import com.taniconnect.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostShareService {

    private final PostShareRepository postShareRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostShareService(
            PostShareRepository postShareRepository,
            PostRepository postRepository,
            UserRepository userRepository) {
        this.postShareRepository = postShareRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostShareResponse sharePost(Long postId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan"));

        if (postShareRepository.existsByPostIdAndUserId(postId, user.getId())) {
            throw new RuntimeException("Anda sudah membagikan post ini");
        }

        PostShare postShare = new PostShare();
        postShare.setPost(post);
        postShare.setUser(user);

        PostShare saved = postShareRepository.save(postShare);

        return new PostShareResponse(
                saved.getId(),
                post.getId(),
                user.getId(),
                user.getUsername(),
                saved.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public long getShareCount(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post tidak ditemukan");
        }

        return postShareRepository.countByPostId(postId);
    }
}
