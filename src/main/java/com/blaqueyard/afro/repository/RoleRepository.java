package com.blaqueyard.afro.repository;

import com.blaqueyard.afro.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findByUserId(Long userId, Pageable pageable);

    //DoubleStream findByIdAndUserId(Long roleId, Long userId);

    Optional<Role> findByIdAndUserId(Long id, Long postId);
}
