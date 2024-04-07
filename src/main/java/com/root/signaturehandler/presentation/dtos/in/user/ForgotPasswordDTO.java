package com.root.signaturehandler.presentation.dtos.in.user;

import com.root.signaturehandler.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDTO {
    @Email(message = "email must be an valid e-mail")
    @NotBlank(message = "email can't be empty")
    @NotEmpty(message = "email can't be empty")
    private String email;

    public User toEntity() {
        User user = new User();
        user.setEmail(this.email);

        return user;
    }
}
