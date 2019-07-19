package com.blaqueyard.afro.controller;

//import com.example.jpa.exception.ResourceNotFoundException;
//import com.example.jpa.model.Post;
//import com.example.jpa.repository.PostRepository;
import com.blaqueyard.afro.exception.ResourceNotFoundException;
import com.blaqueyard.afro.model.User;
import com.blaqueyard.afro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody User userRequest) {
        return userRepository.findById(userId).map(user -> {
            user.setEmail(userRequest.getEmail());
            user.setEncry(userRequest.getEncry());
            user.setUsername(userRequest.getUsername());
            user.setPassword(userRequest.getPassword());
            user.setActivated(userRequest.getActivated());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("UserId %s" + userId + " not found")));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable User userId) {
        return userRepository.findById(userId).map(us -> {
            userRepository.delete(us);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

}
