package com.taniconnect.backend.service;

import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.PostLike;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.PostLikeRepository;
import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostLikeService(
            PostLikeRepository postLikeRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String likePost(Long postId, String username) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post tidak ditemukan"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        if (postLikeRepository.existsByPostIdAndUserId(
                postId, user.getId())) {
            return "Anda sudah menyukai post ini";
        }

        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(user);

        postLikeRepository.save(postLike);

        return "Post berhasil disukai";
    }

    @Transactional
    public String unlikePost(Long postId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        PostLike postLike = postLikeRepository
                .findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Like tidak ditemukan"));

        postLikeRepository.delete(postLike);

        return "Like berhasil dihapus";
    }

    @Transactional(readOnly = true)
    public long countLikes(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post tidak ditemukan");
        }

        return postLikeRepository.countByPostId(postId);
    }
}