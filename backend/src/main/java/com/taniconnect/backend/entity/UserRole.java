
package com.taniconnect.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "user_roles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_role",
            columnNames = {"user_id", "role_id"}
        )
    }
)
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "assigned_at", insertable = false, updatable = false)
    private LocalDateTime assignedAt;

    public UserRole() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Role getRole() { return role; }
    public LocalDateTime getAssignedAt() { return assignedAt; }

    public void setUser(User user) { this.user = user; }
    public void setRole(Role role) { this.role = role; }
}
