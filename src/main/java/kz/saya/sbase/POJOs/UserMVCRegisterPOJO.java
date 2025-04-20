package kz.saya.sbase.POJOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // кастомная аннотация: password == confirmPassword
public class UserMVCRegisterPOJO {

    @NotBlank
    @Size(min = 4, max = 32)
    private String login;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 32)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 32)
    private String lastName;

    private String middleName;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    @NotBlank
    private String confirmPassword;

}
