package com.example.todolist.POJOs;

import com.example.todolist.Entity.User;
import lombok.Getter;
import lombok.Setter;

public record UserResponseEnt(String body, int status, User user) {
}
