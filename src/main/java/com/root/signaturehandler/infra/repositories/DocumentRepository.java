package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.infra.models.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID>, DocumentModel {

    @Override
    @Query("SELECT doc FROM Document doc " +
            " INNER JOIN FETCH doc.folder fold " +
            " WHERE doc.fileName = :documentName AND fold.id = :folderId")
    Optional<Document> findDocumentNameByFolder(Long folderId, String documentName);
}
