
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.CreatePostRequest;
import com.taniconnect.backend.dto.PostResponse;
import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.UserRepository;
import com.taniconnect.backend.repository.PostLikeRepository;
import com.taniconnect.backend.repository.PostCommentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;

    public PostService(
        PostRepository postRepository,
        UserRepository userRepository,
        PostLikeRepository postLikeRepository,
        PostCommentRepository postCommentRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
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

        return toResponse(savedPost, user.getId());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> toResponse(post, user.getId()))
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
        .map(post -> toResponse(post, user.getId()))
        .toList();
    }

    private PostResponse toResponse(Post post, Long currentUserId) {
    return new PostResponse(
            post.getId(),
            post.getUser().getId(),
            post.getUser().getUsername(),
            post.getContent(),
            post.getPostType(),
            post.getCreatedAt(),
            post.getUpdatedAt(),
            postLikeRepository.countByPostId(post.getId()),
            postCommentRepository.countByPostId(post.getId()),
            postLikeRepository.existsByPostIdAndUserId(
                    post.getId(), currentUserId
            )
        );
    }
}
