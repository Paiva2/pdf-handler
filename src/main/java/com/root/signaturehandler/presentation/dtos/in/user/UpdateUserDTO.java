package com.root.signaturehandler.presentation.dtos.in.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.root.signaturehandler.domain.entities.User;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserDTO {
    @Email(message = "email must be an valid e-mail")
    private String email;

    @Size(min = 6, message = "password must have at least 6 characters")
    private String password;

    @Size(min = 3, message = "name must have at least 3 characters")
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
