package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.services.DocumentService;
import com.root.signaturehandler.presentation.dtos.in.document.NewDocumentDTO;
import com.root.signaturehandler.presentation.dtos.out.NewDocumentResponseDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;
    private final JwtAdapter jwtAdapter;

    public DocumentController(DocumentService documentService, JwtAdapter jwtAdapter) {
        this.documentService = documentService;
        this.jwtAdapter = jwtAdapter;
    }

    @PostMapping(value = "/new/{folderId}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_PDF_VALUE
    })
    public ResponseEntity<Object> sendDocument(
            @RequestBody @Valid NewDocumentDTO newDocumentDTO,
            @RequestHeader("Authorization") String authToken,
            @PathVariable(name = "folderId") Long folderId,
            @RequestPart("documents") MultipartFile file
    ) {
        String parseToken = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Document document = new Document();
        Folder folder = new Folder();
        folder.setId(folderId);

        try {
            document.setFileName(file.getOriginalFilename());
            document.setFileBinary(file.getBytes());

            document.setFolder(folder);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            return ResponseEntity.badRequest().body(
                    Collections.singletonMap("message", "Error while getting file bytes...")
            );
        }

        Document documentCreated = this.documentService.sendDocument(
                UUID.fromString(parseToken),
                document,
                newDocumentDTO.getContactsForSend()
        );

        NewDocumentResponseDTO newDocumentResponseDTO = new NewDocumentResponseDTO(
                documentCreated.getId(),
                documentCreated.getFileName(),
                documentCreated.getFolder().getName(),
                documentCreated.getDocumentAttachments().size()
        );

        return ResponseEntity.status(201).body(newDocumentResponseDTO);
    }
}
