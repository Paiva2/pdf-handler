package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.DocumentAttachment;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.infra.models.enums.SendBy;
import com.root.signaturehandler.infra.repositories.DocumentAttachmentRepository;
import com.root.signaturehandler.infra.repositories.DocumentRepository;
import com.root.signaturehandler.infra.repositories.FolderRepository;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class DocumentService {
    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;
    private final DocumentAttachmentRepository documentAttachmentRepository;

    public DocumentService(
            FolderRepository folderRepository,
            DocumentRepository documentRepository,
            DocumentAttachmentRepository documentAttachmentRepository
    ) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.documentAttachmentRepository = documentAttachmentRepository;
    }

    @Transactional
    public Document sendDocument(Document document, ArrayList<DocumentAttachment> attachments) {
        ArrayList<String> requiredFields = new ArrayList<String>() {{
            add("folder");
            add("fileName");
            add("fileBinary");
        }};

        this.serviceDtoValidation(document, requiredFields);

        Optional<Folder> doesFolderExists = this.folderRepository.findById(document.getFolder().getId());

        if (!doesFolderExists.isPresent()) {
            throw new NotFoundException("Folder not found");
        }

        Optional<Document> doesFolderHasDocumentWithName = this.documentRepository.findDocumentNameByFolder(
                doesFolderExists.get().getId(),
                document.getFileName()
        );

        if (doesFolderHasDocumentWithName.isPresent()) {
            document.setFileName(document.getFileName() + "-" + LocalDateTime.now());
        }

        Document createDoc = this.documentRepository.save(document);

        attachments.forEach(attachment -> {
            attachment.setDocument(createDoc);

            if (attachment.getSendBy() == SendBy.EMAIL) {
                //TODO: ADD MAIL SEND
            }
        });
        
        this.documentAttachmentRepository.saveAll(attachments);

        return createDoc;
    }

    private void serviceDtoValidation(Object dto, ArrayList<String> requiredFields) {
        BeanWrapper doc = new BeanWrapperImpl(dto);

        requiredFields.forEach(field -> {
            if (doc.getPropertyValue(field) == null) {
                throw new BadRequestException(field + " can't be null");
            }
        });
    }
}
