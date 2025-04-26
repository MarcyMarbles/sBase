package kz.saya.sbase.POJOs;

import java.io.Serializable;

public record RolePOJO(String name,
                      String description,
                      boolean defaultRole) implements Serializable {
}