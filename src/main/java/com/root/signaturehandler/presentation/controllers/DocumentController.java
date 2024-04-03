package com.root.signaturehandler.presentation.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.services.DocumentService;
import com.root.signaturehandler.presentation.dtos.in.contact.ContactForSendDTO;
import com.root.signaturehandler.presentation.dtos.out.NewDocumentResponseDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
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

    @PostMapping(value = "/new/{folderId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Object> sendDocument(
            @RequestParam(value = "contactsForSend", required = false) String contactsForSend,
            @RequestPart(value = "document") MultipartFile file,
            @RequestHeader("Authorization") String authToken,
            @PathVariable(name = "folderId") Long folderId
    ) throws IOException {
        String parseToken = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Type listDtoType = new TypeToken<ArrayList<ContactForSendDTO>>() {
        }.getType();

        List<ContactForSendDTO> getDtoConverted = new Gson().fromJson(contactsForSend, listDtoType);

        Document document = new Document();
        Folder folder = new Folder();
        folder.setId(folderId);

        document.setFileName(file.getOriginalFilename());
        document.setFileBinary(file.getBytes());
        document.setFolder(folder);
        document.setOriginalFile(file);

        Document documentCreated = this.documentService.sendDocument(
                UUID.fromString(parseToken),
                document,
                getDtoConverted
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
