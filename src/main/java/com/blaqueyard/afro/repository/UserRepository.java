package com.blaqueyard.afro.repository;

import com.blaqueyard.afro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE  u.username = :username")
    User findByUserName(@Param("username") String username);
//    Page<User> findByUserId(Long userId, Pageable pageable);
//    Optional<User> findByIdAndRoleId(Long id, Long roleId);
//    Optional<User> findById(User userId);
}
