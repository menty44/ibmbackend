package com.blaqueyard.afro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

//import java.util.stream.Collector;
//import java.util.stream.Collectors;


@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private UUID encry;

    @NotNull
    @Size(max = 50)
    private String name;

    @Nullable
    @Size(max = 250)
    private String description;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    //    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getEncry() {
        return encry;
    }

    public void setEncry(UUID encry) {
        this.encry = encry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Role(@Nullable UUID encry, @NotNull @Size(max = 50) String name, @Size(max = 250) String description, User... users) {
//        this.encry = encry;
//        this.name = name;
//        this.description = description;
//        this.users = Stream.of(users).collect(Collectors.toSet());
//        this.users.forEach(x-> x.setRole(this));
//
//
//    }
}
