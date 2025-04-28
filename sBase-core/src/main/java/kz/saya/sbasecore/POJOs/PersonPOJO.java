package kz.saya.sbasecore.POJOs;

import java.io.Serializable;

public record PersonPOJO(String firstName,
                         String lastName,
                         String middleName,
                         String email) implements Serializable {
}
