package com.yms.userservice.repository;

import com.yms.userservice.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(@Email(message = "Invalid Email Address") @NotBlank(message = "Email Can Not Be Empty") String email);
}
