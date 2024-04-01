package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Document;

import java.util.Optional;

public interface DocumentModel {
    Optional<Document> findDocumentNameByFolder(Long folderId, String documentName);
}
