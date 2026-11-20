
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.CreatePostRequest;
import com.taniconnect.backend.dto.PostResponse;
import com.taniconnect.backend.dto.UpdatePostRequest;
import com.taniconnect.backend.dto.PostMediaResponse;

import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.User;

import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.UserRepository;
import com.taniconnect.backend.repository.PostLikeRepository;
import com.taniconnect.backend.repository.PostCommentRepository;
import com.taniconnect.backend.repository.PostMediaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostMediaRepository postMediaRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository,
            PostCommentRepository postCommentRepository,
            PostMediaRepository postMediaRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
        this.postMediaRepository = postMediaRepository;
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
    public Page<PostResponse> getAllPosts(
            String username,
            int page,
            int size
    ) {
        if (page < 0 || size < 1 || size > 50) {
            throw new IllegalArgumentException(
                    "Page harus >= 0 dan size harus antara 1-50"
            );
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return postRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(post -> toResponse(post, user.getId()));
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

    @Transactional
    public PostResponse updatePost(
            Long postId,
            String username,
            UpdatePostRequest request
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post tidak ditemukan"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Anda tidak memiliki izin mengedit post ini"
            );
        }

        post.setContent(request.content());
        post.setPostType(request.postType());

        return toResponse(post, user.getId());
    }

    @Transactional
    public void deletePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post tidak ditemukan"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Anda tidak memiliki izin menghapus post ini"
            );
        }

        postRepository.delete(post);
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

    private PostResponse toResponse(
            Post post,
            Long currentUserId
    ) {
        List<PostMediaResponse> media = postMediaRepository
                .findByPostIdOrderByDisplayOrderAsc(post.getId())
                .stream()
                .map(item -> new PostMediaResponse(
                        item.getId(),
                        post.getId(),
                        item.getMediaUrl(),
                        item.getMediaType(),
                        item.getDisplayOrder(),
                        item.getCreatedAt()
                ))
                .toList();

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
                ),
                media
        );
    }
}
