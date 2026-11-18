
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.CreatePostRequest;
import com.taniconnect.backend.dto.PostResponse;
import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostResponse createPost(
            String username,
            CreatePostRequest request
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(request.content());
        post.setPostType(request.postType());

        Post savedPost = postRepository.save(post);

        return toResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getMyPosts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        return postRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getContent(),
                post.getPostType(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
