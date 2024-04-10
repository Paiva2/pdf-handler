package com.root.signaturehandler.presentation.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.services.DocumentService;
import com.root.signaturehandler.infra.models.enums.DocumentsOrderBy;
import com.root.signaturehandler.presentation.dtos.in.contact.ContactForSendDTO;
import com.root.signaturehandler.presentation.dtos.out.AllDocumentsDTO;
import com.root.signaturehandler.presentation.dtos.out.DocumentResponseDTO;
import com.root.signaturehandler.presentation.dtos.out.NewDocumentResponseDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

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

    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(name = "documentId") UUID documentId
    ) {
        String parseTokenSub = this.jwtAdapter.verify(authToken.replace("Bearer", ""));

        this.documentService.deleteDocument(
                UUID.fromString(parseTokenSub),
                documentId
        );

        return ResponseEntity.status(204).build();
    }

    @GetMapping("/list")
    public ResponseEntity<AllDocumentsDTO> listAllDocuments(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int perPage,
            @RequestParam(name = "file", required = false) String fileName,
            @RequestParam(name = "order", required = false, defaultValue = "ASC") DocumentsOrderBy orderBy
    ) {
        String parseTokenSub = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Page<Document> documents = this.documentService.listAll(
                UUID.fromString(parseTokenSub),
                page,
                perPage,
                fileName,
                orderBy.getOrderBy()
        );

        List<DocumentResponseDTO> documentResponseDtos = documents.getContent().stream()
                .map(doc -> new DocumentResponseDTO(
                        doc.getId(),
                        doc.getDocumentUrl(),
                        doc.getDisabled(),
                        doc.getCreatedAt(),
                        doc.getDeletedAt()
                )).collect(Collectors.toList());

        AllDocumentsDTO allDocumentsDTO = new AllDocumentsDTO(
                documentResponseDtos,
                documents.getTotalElements(),
                documents.getTotalPages(),
                documents.getSize(),
                documents.getNumber() + 1
        );
        
        return ResponseEntity.status(200).body(allDocumentsDTO);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDTO> filterDocument(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("documentId") UUID documentId
    ) {
        String parseTokenSub = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Document document = this.documentService.filterDocument(
                UUID.fromString(parseTokenSub),
                documentId
        );

        DocumentResponseDTO documentResponseDTO = new DocumentResponseDTO(
                document.getId(),
                document.getDocumentUrl(),
                document.getDisabled(),
                document.getCreatedAt(),
                document.getDeletedAt()
        );


        return ResponseEntity.status(200).body(documentResponseDTO);
    }
}
