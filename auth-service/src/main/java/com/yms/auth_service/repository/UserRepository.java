package com.yms.auth_service.repository;

import com.yms.auth_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String username);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    Page<User> findAllActives(Pageable pageable);
}
