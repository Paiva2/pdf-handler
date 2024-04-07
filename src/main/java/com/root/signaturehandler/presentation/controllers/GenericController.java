package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.utils.FileUploaderAdapter;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/generic")
public class GenericController {
    private final FileUploaderAdapter fileUploaderAdapter;
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB in bytes

    public GenericController(FileUploaderAdapter fileUploaderAdapter) {
        this.fileUploaderAdapter = fileUploaderAdapter;
    }

    @PostMapping(value = "/upload-image", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> upload(
            @RequestPart(value = "file") MultipartFile file
    ) {
        if (file == null) throw new BadRequestException("File can't be null");

        if (file.getOriginalFilename() == null) throw new BadRequestException("File name can't be null");

        if (file.getSize() > this.MAX_FILE_SIZE) throw new BadRequestException("File can't have more than 5MB");

        if (!file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)
                && !file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)
                && !file.getContentType().equals("image/jpg")
        ) {
            throw new BadRequestException("File must be jpg, png or jpeg only");
        }

        try {
            String fileName = UUID.randomUUID().toString();

            this.fileUploaderAdapter.setDocumentsDestination("generic-images-app");

            String fileUrl = this.fileUploaderAdapter.uploadDocument(
                    file,
                    fileName
            );

            HashMap<String, String> responseBody = new HashMap<String, String>() {{
                put("fileName", fileName);
                put("url", fileUrl);
            }};

            return ResponseEntity.status(201).body(responseBody);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        return ResponseEntity.status(500).body(
                Collections.singletonMap("message", "Error while uploading the file...")
        );
    }
}
