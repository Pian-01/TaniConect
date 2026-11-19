
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.CommentResponse;
import com.taniconnect.backend.dto.CreateCommentRequest;
import com.taniconnect.backend.dto.UpdateCommentRequest;
import com.taniconnect.backend.entity.Post;
import com.taniconnect.backend.entity.PostComment;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.PostCommentRepository;
import com.taniconnect.backend.repository.PostRepository;
import com.taniconnect.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostCommentService {

    private final PostCommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostCommentService(
            PostCommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
            
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public CommentResponse updateComment(
        Long commentId,
        String username,
        UpdateCommentRequest request
    ) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new RuntimeException("User tidak ditemukan"));

        PostComment comment = commentRepository
                .findByIdAndUserId(commentId, user.getId())
                .orElseThrow(() ->
                    new RuntimeException(
                            "Komentar tidak ditemukan atau bukan milik Anda"
                    ));

            comment.setContent(request.content());

        return toResponse(comment, List.of());
    }


    @Transactional(readOnly = true)
    public long countComments(Long postId) {
            if (!postRepository.existsById(postId)) {
        throw new RuntimeException("Post tidak ditemukan");
        }

        return commentRepository.countByPostId(postId);
    }


    @Transactional
    public CommentResponse createComment(
            Long postId,
            String username,
            CreateCommentRequest request
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException("Post tidak ditemukan"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        PostComment parentComment = null;

        if (request.parentCommentId() != null) {
            parentComment = commentRepository
                    .findById(request.parentCommentId())
                    .orElseThrow(() ->
                            new RuntimeException("Komentar induk tidak ditemukan"));

            if (!parentComment.getPost().getId().equals(postId)) {
                throw new RuntimeException(
                        "Komentar induk bukan bagian dari post ini"
                );
            }
        }

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setParentComment(parentComment);
        comment.setContent(request.content());

        PostComment savedComment = commentRepository.save(comment);

        return toResponse(savedComment, List.of());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post tidak ditemukan");
        }

        List<PostComment> comments =
                commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        Map<Long, List<PostComment>> repliesMap = comments.stream()
                .filter(comment -> comment.getParentComment() != null)
                .collect(Collectors.groupingBy(
                        comment -> comment.getParentComment().getId(),
                        HashMap::new,
                        Collectors.toList()
                ));

        return comments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(comment -> buildCommentTree(comment, repliesMap))
                .toList();
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        PostComment comment = commentRepository
                .findByIdAndUserId(commentId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Komentar tidak ditemukan atau bukan milik Anda"
                        ));

        if (!comment.getReplies().isEmpty()) {
            throw new RuntimeException(
                    "Komentar masih memiliki balasan dan tidak dapat dihapus"
            );
        }

        commentRepository.delete(comment);
    }

    private CommentResponse buildCommentTree(
            PostComment comment,
            Map<Long, List<PostComment>> repliesMap
    ) {
        List<PostComment> childComments =
                repliesMap.getOrDefault(comment.getId(), new ArrayList<>());

        List<CommentResponse> replies = childComments.stream()
                .map(child -> buildCommentTree(child, repliesMap))
                .toList();

        return toResponse(comment, replies);
    }

    private CommentResponse toResponse(
            PostComment comment,
            List<CommentResponse> replies
    ) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getContent(),
                comment.getParentComment() != null
                        ? comment.getParentComment().getId()
                        : null,
                comment.getCreatedAt(),
                replies
        );
    }
}
