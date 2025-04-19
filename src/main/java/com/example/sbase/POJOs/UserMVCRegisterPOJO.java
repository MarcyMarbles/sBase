package com.example.sbase.POJOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMVCRegisterPOJO {
    String username;
    String password;
    String confirmPassword;
    String firstName;
    String lastName;
    String middleName;
    String email;
}
