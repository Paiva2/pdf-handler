package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NewDocumentResponseDTO {
    public UUID documentId;

    public String fileName;

    public String folderName;

    public int attachmentsCreated;
}
