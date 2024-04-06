package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class ContactResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String createdAt;
}
