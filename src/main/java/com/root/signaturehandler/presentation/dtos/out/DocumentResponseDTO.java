package com.root.signaturehandler.presentation.dtos.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDTO {
    private UUID id;
    private String documentUrl;
    private List<DocumentAttachmentResponseDTO> documentAttachments;
    private boolean disabled;
    private String createdAt;
    private Date deletedAt;
}
