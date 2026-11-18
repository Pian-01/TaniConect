package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.UserProfileResponse;
import com.taniconnect.backend.dto.UpdateProfileRequest;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pengguna tidak ditemukan")
                );

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getProfilePicture(),
                user.getBio(),
                user.getStatus()
        );
    }
    @Transactional
public UserProfileResponse updateProfile(
        String username,
        UpdateProfileRequest request
) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new IllegalArgumentException("Pengguna tidak ditemukan")
            );

    user.setFullName(request.fullName());
    user.setPhone(request.phone());
    user.setBio(request.bio());
    user.setProfilePicture(request.profilePicture());

    userRepository.save(user);

    return new UserProfileResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getPhone(),
            user.getProfilePicture(),
            user.getBio(),
            user.getStatus()
    );
}
}