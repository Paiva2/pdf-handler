package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.DocumentAttachment;
import com.root.signaturehandler.infra.models.DocumentAttachmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentAttachmentRepository extends JpaRepository<DocumentAttachment, UUID>, DocumentAttachmentModel {
}
