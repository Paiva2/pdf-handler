package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ContactResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Date createdAt;
}
