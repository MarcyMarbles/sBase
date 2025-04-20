package kz.saya.sbase.POJOs;

import kz.saya.sbase.Entity.User;

public record UserResponseEnt(String body, int status, User user) {
}
