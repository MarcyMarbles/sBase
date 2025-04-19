package com.example.todolist.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Person extends MappedSuperClass {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    @JsonIgnore
    private List<Tasks> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "person_to_user",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @OneToOne
    private FileDescriptor profilePicture;

    public User getCurrentUser(){
        if(users.isEmpty()){
            return null;
        }
        OffsetDateTime now = OffsetDateTime.now();
        return users
                .stream()
                .filter(e -> e.getStart_date_ts().isBefore(now) && e.getEnd_date_ts().isAfter(now))
                .findFirst().orElse(null);
    }
}
