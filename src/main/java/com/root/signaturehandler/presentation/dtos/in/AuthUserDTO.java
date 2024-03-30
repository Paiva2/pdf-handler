package com.root.signaturehandler.presentation.dtos.in;

import com.root.signaturehandler.domain.entities.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AuthUserDTO {
    @Email(message = "email must be an valid e-mail")
    @NotBlank(message = "email can't be empty")
    @NotNull(message = "email can't be null")
    private String email;

    @NotBlank(message = "password can't be empty")
    @NotNull(message = "email can't be null")
    private String password;

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);

        return user;
    }
}
