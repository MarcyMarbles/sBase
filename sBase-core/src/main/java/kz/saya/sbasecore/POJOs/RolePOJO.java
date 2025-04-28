package kz.saya.sbasecore.POJOs;

import java.io.Serializable;

public record RolePOJO(String name,
                      String description,
                      boolean defaultRole) implements Serializable {
}
