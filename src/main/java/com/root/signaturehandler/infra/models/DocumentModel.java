package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface DocumentModel {
    Optional<Document> findDocumentNameByFolder(Long folderId, String documentName);

    Optional<Document> findByIdAndUserId(UUID documentId, UUID userId);

    Page<Document> findAll(Specification<Document> specification, Pageable pageable);
}
