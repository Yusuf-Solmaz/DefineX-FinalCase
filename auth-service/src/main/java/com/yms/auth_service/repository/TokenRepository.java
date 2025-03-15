package com.yms.auth_service.repository;

import com.yms.auth_service.entity.Token;
import com.yms.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
    void deleteByUser(User user);
}
