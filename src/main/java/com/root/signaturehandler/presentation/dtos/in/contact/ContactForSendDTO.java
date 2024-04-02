package com.root.signaturehandler.presentation.dtos.in.contact;

import com.root.signaturehandler.infra.models.enums.SendBy;
import lombok.Data;

import java.util.UUID;

@Data
public class ContactForSendDTO {
    private UUID id;

    private String email;

    private String phone;

    private SendBy sendBy;
}
