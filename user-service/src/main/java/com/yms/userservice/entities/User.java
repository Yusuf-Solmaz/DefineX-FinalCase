package com.yms.userservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;

    @Email(message = "Invalid Email Address")
    @NotBlank(message = "Email Can Not Be Empty")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password Can Not Be Empty")
    @Size(min = 6, message = "Password Must Be At Least 6 Character")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Name Can Not Be Empty")
    @Size(min = 3, message = "Password Must Be At Least 3 Character")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

}
