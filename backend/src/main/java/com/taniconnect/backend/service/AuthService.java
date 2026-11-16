
package com.taniconnect.backend.service;

import com.taniconnect.backend.dto.LoginRequest;
import com.taniconnect.backend.dto.LoginResponse;
import com.taniconnect.backend.dto.RegisterRequest;
import com.taniconnect.backend.dto.RegisterResponse;
import com.taniconnect.backend.entity.Role;
import com.taniconnect.backend.entity.User;
import com.taniconnect.backend.entity.UserRole;
import com.taniconnect.backend.repository.RoleRepository;
import com.taniconnect.backend.repository.UserRepository;
import com.taniconnect.backend.repository.UserRoleRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username sudah digunakan");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email sudah digunakan");
        }

        Role buyerRole = roleRepository.findByName("BUYER")
                .orElseThrow(() ->
                        new IllegalStateException("Role BUYER tidak ditemukan")
                );

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setStatus("ACTIVE");

        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(buyerRole);

        userRoleRepository.save(userRole);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String identifier = request.usernameOrEmail().trim();

        String username = userRepository.findByEmail(identifier)
                .map(User::getUsername)
                .orElse(identifier);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.password()
                )
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalStateException("Pengguna tidak ditemukan")
                );

        List<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
    }
}
