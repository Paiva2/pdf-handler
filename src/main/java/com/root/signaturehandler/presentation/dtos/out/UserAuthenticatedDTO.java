package com.root.signaturehandler.presentation.dtos.out;

import lombok.Data;

@Data
public class UserAuthenticatedDTO {
    private String message = "Authenticated successfully!";

    private String authToken;

    public UserAuthenticatedDTO(String token) {
        this.authToken = token;
    }
}
