package com.blaqueyard.afro.corelogic;

import com.blaqueyard.afro.model.Role;
import com.blaqueyard.afro.model.User;
import com.blaqueyard.afro.enumbool.activated;
import com.blaqueyard.afro.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class test {
    @Autowired
    RoleRepository roleRepository;

    public static void main(String[]args){
        int num = 12;
        String string = String.format("A string %s", num);
        System.out.println(string);

        //User user = new User("d01f89a4-f0c5-475b-aab3-f87230937185", "menty44@gmail.com", "menty44", "1234abc", activated.active  );
        Role role = new Role();
        role.setId(Long.valueOf(1));
        role.setName("Admin");
        role.setName("the super user");


        System.out.println("data to be saved: %role  " + role);

    }
}
