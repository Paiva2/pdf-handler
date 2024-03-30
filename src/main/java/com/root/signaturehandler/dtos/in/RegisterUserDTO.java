package com.root.signaturehandler.dtos.in;


import com.root.signaturehandler.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterUserDTO {
    @NotNull(message = "name can't be null")
    @NotBlank(message = "name can't be empty")
    private String name;

    @NotNull(message = "email can't be null")
    @NotBlank(message = "email can't be empty")
    @Email(message = "email must be an valid e-mail")
    private String email;

    @NotNull(message = "password can't be null")
    @NotBlank(message = "password can't be empty")
    @Length(min = 6, message = "password must have at least 6 characters")
    private String password;

    public User toUser() {
        User user = new User();
        user.setName(this.name);
        user.setEmail(this.email);
        user.setPassword(this.password);

        return user;
    }
}
