package com.blaqueyard.afro.repository;

import com.blaqueyard.afro.model.Role;
import com.blaqueyard.afro.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserId(Long userId, Pageable pageable);
    Optional<User> findByIdAndRoleId(Long id, Long roleId);
    Optional<User> findById(User userId);
}
