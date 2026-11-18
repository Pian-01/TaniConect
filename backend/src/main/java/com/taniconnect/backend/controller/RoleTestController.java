package com.taniconnect.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/roles")
public class RoleTestController {

    @GetMapping("/buyer")
    @PreAuthorize("hasRole('BUYER')")
    public String buyerAccess() {
        return "Akses BUYER berhasil";
    }

    @GetMapping("/farmer")
    @PreAuthorize("hasRole('FARMER')")
    public String farmerAccess() {
        return "Akses FARMER berhasil";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Akses ADMIN berhasil";
    }
}