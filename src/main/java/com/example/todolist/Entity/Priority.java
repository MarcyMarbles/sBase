package com.example.todolist.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Priority extends MappedLocalizedClass {
    private String name;

    @JsonIgnore
    @ManyToOne
    private Person userId;

    private String color = "#00000";
}
