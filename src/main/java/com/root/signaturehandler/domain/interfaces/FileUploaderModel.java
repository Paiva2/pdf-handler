package com.root.signaturehandler.domain.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploaderModel {
    String uploadDocument(MultipartFile file, String newFileName) throws IOException;

    void setDocumentsDestination(String destination);
}
