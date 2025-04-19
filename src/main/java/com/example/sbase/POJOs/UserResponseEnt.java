package com.example.sbase.POJOs;

import com.example.sbase.Entity.User;
import lombok.Getter;
import lombok.Setter;

public record UserResponseEnt(String body, int status, User user) {
}
