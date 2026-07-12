package com.codems.audittrail.domain.user.model;

import com.codems.audittrail.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 180)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 40)
    private Role role;

    private User(String fullName, String email, String password, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User of(String fullName, String email, String password, Role role) {
        return new User(fullName.trim(), email.trim().toLowerCase(Locale.ROOT), password, role);
    }

    public void updateProfile(String fullName) {
        this.fullName = fullName;
    }

    public void changeRole(Role role) {
        this.role = role;
    }
}
