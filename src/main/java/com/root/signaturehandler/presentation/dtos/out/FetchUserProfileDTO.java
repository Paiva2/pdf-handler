package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FetchUserProfileDTO {
    private UUID id;
    private String email;
    private String name;
    private Date createdAt;
}
