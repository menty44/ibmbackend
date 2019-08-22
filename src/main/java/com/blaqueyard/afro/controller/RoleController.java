package com.blaqueyard.afro.controller;


import com.blaqueyard.afro.exception.ResourceNotFoundException;
import com.blaqueyard.afro.model.Role;
import com.blaqueyard.afro.repository.RoleRepository;
import com.blaqueyard.afro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    private UUID uuid = UUID.randomUUID();

    @RequestMapping(value = "/products")
    public String getProductName() {
        return "Honey";
    }

//    @GetMapping("/users/{userId}/roles")
//    public Page<Role> getAllRolesByUserId(@PathVariable(value = "userId") Long userId,
//                                             Pageable pageable) {
//        return roleRepository.findByUserId(userId, pageable);
//    }

    //new role
    //new user reg
    @RequestMapping(value = "roles", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map<String,String>> registerNewUser(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description) throws IOException {

        Map<String, String> response = new HashMap<String, String>();

        if (name != null && !name.isEmpty() && description != null && !description.isEmpty()) {

            Role roleCheckName = roleRepository.findByName(name);

            if(roleCheckName == null){
                Role role = new Role();
                role.setEncry(uuid);
                role.setName(name);
                role.setDescription(description);

                roleRepository.save(role);

                response.put("ok", "save success");
                response.put("code", "00");
                return ResponseEntity.accepted().body(response);
            }else {

                response.put("mg", "fail");
                response.put("code", "03");
                response.put("desc", "name already exists");
                return ResponseEntity.ok().body(response);
            }
        } else {
            String ts = "one of the parameters is missing";
            response.put("error", ts);
            response.put("code", "05");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/users/{userId}/roles")
    public Role createRole(@PathVariable (value = "userId") Long userId,
                              @Valid @RequestBody Role role) {
        return userRepository.findById(userId).map(us -> {
//            role.setUser(us);
//            role.setName(role.getName("admin").toString());
//            role.setUsers(us);
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


//    @DeleteMapping("/users/{userId}/roles/{roleId}")
//    public ResponseEntity<?> deleteRole(@PathVariable (value = "userId") Long userId,
//                                           @PathVariable (value = "roleId") Long roleId) {
//        return roleRepository.findByIdAndUserId(roleId, userId).map(rol -> {
//            roleRepository.delete(rol);
//            return ResponseEntity.ok().build();
//        }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId + " and userId " + userId));
//    }





}
