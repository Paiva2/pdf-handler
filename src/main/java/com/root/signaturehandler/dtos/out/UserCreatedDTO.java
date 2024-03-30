package com.root.signaturehandler.dtos.out;

import com.root.signaturehandler.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserCreatedDTO {
    private String message = "User created successfully!";

    private String email;

    private String name;

    private Date createdAt;

    public UserCreatedDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}
