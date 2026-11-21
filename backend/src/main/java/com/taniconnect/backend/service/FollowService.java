
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.FollowResponse;
import com.taniconnect.backend.entity.Follow;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.FollowRepository;
import com.taniconnect.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(
            FollowRepository followRepository,
            UserRepository userRepository
    ) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FollowResponse followUser(
            String username,
            Long targetUserId
    ) {
        User follower = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        User following = userRepository.findById(targetUserId)
                .orElseThrow(() ->
                        new RuntimeException("Target user tidak ditemukan"));

        if (follower.getId().equals(following.getId())) {
            throw new RuntimeException("Tidak bisa mengikuti diri sendiri");
        }

        if (followRepository.existsByFollowerIdAndFollowingId(
                follower.getId(), following.getId())) {
            throw new RuntimeException("Sudah mengikuti user ini");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        Follow savedFollow = followRepository.save(follow);

        return toResponse(savedFollow);
    }

    @Transactional
    public void unfollowUser(
            String username,
            Long targetUserId
    ) {
        User follower = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User tidak ditemukan"));

        Follow follow = followRepository
                .findByFollowerIdAndFollowingId(
                        follower.getId(), targetUserId
                )
                .orElseThrow(() ->
                        new RuntimeException("Belum mengikuti user ini"));

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public long getFollowersCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User tidak ditemukan");
        }

        return followRepository.countByFollowingId(userId);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User tidak ditemukan");
        }

        return followRepository.countByFollowerId(userId);
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowers(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User tidak ditemukan");
        }

        return followRepository
                .findByFollowingIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowing(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User tidak ditemukan");
        }

        return followRepository
                .findByFollowerIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FollowResponse toResponse(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollower().getUsername(),
                follow.getFollowing().getId(),
                follow.getFollowing().getUsername(),
                follow.getCreatedAt()
        );
    }
}
