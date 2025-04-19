package com.example.sbase.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Roles extends MappedSuperClass{
    private String name;
    private String description;
    private boolean defaultRole = false;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Roles(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Roles() {}
}
