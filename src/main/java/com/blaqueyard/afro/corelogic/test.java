package com.blaqueyard.afro.corelogic;

import com.blaqueyard.afro.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class test {
    @Autowired
    static
    RoleRepository roleRepository;

    public static void main(String[] args)throws Exception{
        int num = 12;
        String string = String.format("A string %s", num);
        System.out.println(string);
        // creating UUID
        UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        String password = BCrypt.hashpw("menty44", BCrypt.gensalt(12));
        System.out.println(password);

        String passwordw = "menty44";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(passwordw);

        System.out.println(hashedPassword);

        // Create a couple of Book and BookCategory
        //bookCategoryRepository.save(new BookCategory("Category 1", new Book("Hello Koding 1"), new Book("Hello Koding 2")));
//        roleRepository.save(new Role(uid,"admin","super user of the system", new User(uid,"menty44@gmail.com","menty44","menty44","true")));

    }
}
