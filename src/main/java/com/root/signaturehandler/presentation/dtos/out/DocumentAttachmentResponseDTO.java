package com.root.signaturehandler.presentation.dtos.out;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.infra.models.enums.SendBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAttachmentResponseDTO {
    private Long id;
    private String createdAt;
    private SendBy sendBy;
    private ContactResponseDTO contact;
}
