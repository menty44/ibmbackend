package com.blaqueyard.afro.controller;

//import com.example.jpa.exception.ResourceNotFoundException;
//import com.example.jpa.model.Post;
//import com.example.jpa.repository.PostRepository;

import com.blaqueyard.afro.exception.ResourceNotFoundException;
import com.blaqueyard.afro.model.User;
import com.blaqueyard.afro.repository.RoleRepository;
import com.blaqueyard.afro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private UUID uuid = UUID.randomUUID();

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    //new user reg
    @CrossOrigin
    @RequestMapping(value = "newemployer", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map<String,String>> registerNewUser(
            @RequestParam(value = "companyname") String email,
            @RequestParam(value = "website") String username,
            @RequestParam(value = "mobile") String password,
            @RequestParam(value = "location") String activated) throws IOException {

        Map<String, String> response = new HashMap<String, String>();

        if (email != null && !email.isEmpty() && username != null && !username.isEmpty() && password != null && !password.isEmpty()
                && activated != null && !activated.isEmpty()) {

            User userCheckEmail = userRepository.findByEmail(email);

            if(userCheckEmail == null){
                User user = new User();
                user.setEncry(uuid);
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(password);
                user.setActivated(com.blaqueyard.afro.enumbool.activated.inactive.toString());
                user.setRole("Admin");
                response.put("ok", "save success");
                response.put("code", "00");
                return ResponseEntity.accepted().body(response);
            }else {

                response.put("mg", "fail");
                response.put("code", "03");
                response.put("desc", "user already registered");
                return ResponseEntity.ok().body(response);
            }
        } else {
            String ts = "one of the parameters is missing";
            response.put("error", ts);
            response.put("code", "05");
            return ResponseEntity.badRequest().body(response);
        }
    }

//    @PostMapping("/users")
//    public User createUser(@Valid @RequestBody User user) {
//        return userRepository.save(user);
//    }

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

//    @DeleteMapping("/users/{userId}")
//    public ResponseEntity<?> deleteUser(@PathVariable User usancoserId) {
//        return userRepository.findById(userId).map(us -> {
//            userRepository.delete(us);
//            return ResponseEntity.ok().build();
//        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
//    }

    @GetMapping("/users/test")
    public ResponseEntity<?> getAllUsers(){
        Map<String, String > response = new HashMap<String, String>();
        // creating UUID
        UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        // Create a couple of Book and BookCategory
        //bookCategoryRepository.save(new BookCategory("Category 1", new Book("Hello Koding 1"), new Book("Hello Koding 2")));
//        roleRepository.save(new Role(uid,"admin","super user of the system", new User(uid,"menty44@gmail.com","menty44","menty44","true")));
//        roleRepository.save(new User(uid,"menty44@gmail.com","menty44","menty44","true")));

        response.put("msg","ok");
        return ResponseEntity.accepted().body(response);
    }

}
