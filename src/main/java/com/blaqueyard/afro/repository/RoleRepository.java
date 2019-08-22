package com.blaqueyard.afro.repository;

import com.blaqueyard.afro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

//    Page<Role> findByUserId(Long userId, Pageable pageable);
//
//    //DoubleStream findByIdAndUserId(Long roleId, Long userId);
//
//    Optional<Role> findByIdAndUserId(Long id, Long postId);
}
