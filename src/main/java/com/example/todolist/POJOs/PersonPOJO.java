package com.example.todolist.POJOs;

import java.io.Serializable;

public record PersonPOJO(String firstName,
                         String lastName,
                         String middleName,
                         String email) implements Serializable {
}