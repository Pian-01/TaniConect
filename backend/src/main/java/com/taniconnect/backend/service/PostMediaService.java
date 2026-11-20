
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.CreatePostMediaRequest;
import com.taniconnect.backend.dto.PostMediaResponse;
import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.PostMedia;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.PostMediaRepository;
import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostMediaService {

    private final PostMediaRepository postMediaRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostMediaService(
            PostMediaRepository postMediaRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.postMediaRepository = postMediaRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostMediaResponse addMedia(
            Long postId,
            String username,
            CreatePostMediaRequest request
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post tidak ditemukan"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Anda tidak memiliki izin menambahkan media"
            );
        }

        PostMedia media = new PostMedia();
        media.setPost(post);
        media.setMediaUrl(request.mediaUrl());
        media.setMediaType(request.mediaType());
        media.setDisplayOrder(
                request.displayOrder() == null ? 0 : request.displayOrder()
        );

        return toResponse(postMediaRepository.save(media));
    }

    @Transactional(readOnly = true)
    public List<PostMediaResponse> getPostMedia(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post tidak ditemukan");
        }

        return postMediaRepository
                .findByPostIdOrderByDisplayOrderAsc(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteMedia(Long mediaId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        PostMedia media = postMediaRepository.findById(mediaId)
                .orElseThrow(() ->
                        new RuntimeException("Media tidak ditemukan"));

        if (!media.getPost().getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Anda tidak memiliki izin menghapus media ini"
            );
        }

        postMediaRepository.delete(media);
    }

    private PostMediaResponse toResponse(PostMedia media) {
        return new PostMediaResponse(
                media.getId(),
                media.getPost().getId(),
                media.getMediaUrl(),
                media.getMediaType(),
                media.getDisplayOrder(),
                media.getCreatedAt()
        );
    }
}
