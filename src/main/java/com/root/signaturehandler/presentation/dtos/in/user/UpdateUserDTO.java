package com.root.signaturehandler.presentation.dtos.in.user;

import com.root.signaturehandler.domain.entities.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class UpdateUserDTO {
    @NotBlank(message = "email can't be empty")
    @Email(message = "email must be an valid e-mail")
    private String email;

    @NotBlank(message = "password can't be empty")
    @Size(min = 6, message = "password must have at least 6 characters")
    private String password;

    @NotBlank(message = "name can't be empty")
    private String name;

    public User toUser(UUID id) {
        User user = new User();
        user.setId(id);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setName(this.name);

        return user;
    }
}
