package kz.saya.sbasecore.POJOs;

import kz.saya.sbasecore.Entity.User;

public record UserResponseEnt(String body, int status, User user) {
}
