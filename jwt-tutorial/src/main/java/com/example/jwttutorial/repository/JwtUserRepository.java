package com.example.jwttutorial.repository;

import com.example.jwttutorial.entity.JwtUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtUserRepository extends JpaRepository<JwtUser, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<JwtUser> findOneWithAuthoritiesByUsername(String username);
}
