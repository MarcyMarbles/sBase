package com.example.todolist.POJOs;

import java.io.Serializable;

public record UserAuthPOJO(String login, String password) implements Serializable{}
