package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.services.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/new", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_PDF_VALUE
    })
    public ResponseEntity<Object> sendDocument(@RequestPart("documents") MultipartFile file) {

        //documents.get(0).getBytes()

        //this.documentService.sendDocument();

        return ResponseEntity.status(200).body("");
    }
}
