package kz.saya.sbasecore.POJOs;

import java.io.Serializable;

public record UserAuthPOJO(String login, String password) implements Serializable{}
