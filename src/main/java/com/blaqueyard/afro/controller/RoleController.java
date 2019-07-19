package com.blaqueyard.afro.controller;


import com.blaqueyard.afro.exception.ResourceNotFoundException;
import com.blaqueyard.afro.model.Role;
import com.blaqueyard.afro.model.User;
import com.blaqueyard.afro.repository.RoleRepository;
import com.blaqueyard.afro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/{userId}/roles")
    public Page<Role> getAllRolesByUserId(@PathVariable(value = "userId") Long userId,
                                             Pageable pageable) {
        return roleRepository.findByUserId(userId, pageable);
    }

    @PostMapping("/users/{userId}/roles")
    public Role createRole(@PathVariable (value = "userId") Long userId,
                              @Valid @RequestBody Role role) {
        return userRepository.findById(userId).map(us -> {
            role.setUser(us);
            return roleRepository.save(role);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PutMapping("/users/{userId}/roles/{roleId}}")
    public Role updateRole(@PathVariable (value = "userId") Long userId,
                                 @PathVariable (value = "roleId") Long roleId,
                                 @Valid @RequestBody Role roleRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return roleRepository.findById(roleId).map(rol -> {
            rol.setName(roleRequest.getName());
            rol.setDescription(roleRequest.getDescription());
            return roleRepository.save(rol);
        }).orElseThrow(() -> new ResourceNotFoundException("RoleId " + roleId + "not found"));
    }


    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable (value = "userId") Long userId,
                                           @PathVariable (value = "roleId") Long roleId) {
        return roleRepository.findByIdAndUserId(roleId, userId).map(rol -> {
            roleRepository.delete(rol);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId + " and userId " + userId));
    }





}
