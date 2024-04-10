package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.infra.models.DocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
public interface DocumentRepository extends
        JpaRepository<Document, UUID>, DocumentModel, JpaSpecificationExecutor<Document> {

    @Override
    @Query("SELECT doc FROM Document doc " +
            " INNER JOIN FETCH doc.folder fold " +
            " WHERE doc.fileName = :documentName AND fold.id = :folderId")
    Optional<Document> findDocumentNameByFolder(Long folderId, String documentName);

    @Override
    @Query("SELECT d FROM Document d " +
            "INNER JOIN FETCH d.folder f " +
            "INNER JOIN FETCH f.user u " +
            "WHERE d.id = :documentId AND u.id = :userId"
    )
    Optional<Document> findByIdAndUserId(UUID documentId, UUID userId);

    @Override
    Page<Document> findAll(Specification<Document> specification, Pageable pageable);
}
