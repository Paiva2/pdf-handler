package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.DocumentAttachment;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.utils.EmailHandlerAdapter;
import com.root.signaturehandler.domain.utils.FileUploaderAdapter;
import com.root.signaturehandler.infra.models.enums.SendBy;
import com.root.signaturehandler.infra.repositories.ContactRepository;
import com.root.signaturehandler.infra.repositories.DocumentAttachmentRepository;
import com.root.signaturehandler.infra.repositories.DocumentRepository;
import com.root.signaturehandler.infra.repositories.FolderRepository;
import com.root.signaturehandler.presentation.dtos.in.contact.ContactForSendDTO;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.MailNotFoundException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Value("${aws.bucket.documents.name}")
    private String destinationForDocuments;

    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;
    private final ContactRepository contactRepository;
    private final DocumentAttachmentRepository documentAttachmentRepository;
    private final EmailHandlerAdapter emailHandlerAdapter;
    private final FileUploaderAdapter fileUploaderAdapter;

    public DocumentService(
            FolderRepository folderRepository,
            DocumentRepository documentRepository,
            DocumentAttachmentRepository documentAttachmentRepository,
            ContactRepository contactRepository,
            EmailHandlerAdapter emailHandlerAdapter,
            FileUploaderAdapter fileUploaderAdapter
    ) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.documentAttachmentRepository = documentAttachmentRepository;
        this.emailHandlerAdapter = emailHandlerAdapter;
        this.contactRepository = contactRepository;
        this.fileUploaderAdapter = fileUploaderAdapter;
    }

    @Transactional(noRollbackFor = MailNotFoundException.class)
    public Document sendDocument(
            UUID userId,
            Document document,
            List<ContactForSendDTO> contactsForSendDto) {
        if (userId == null) {
            throw new BadRequestException("userId can't be empty");
        }

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
            document.setFileName(this.randomFileName(document.getFileName()));
        }

        document.setFolder(doesFolderExists.get());
        Document createDoc = this.documentRepository.save(document);

        List<Contact> userContactsToReceive = this.contactRepository.findContactsByIdByUserId(
                userId,
                contactsForSendDto
                        .stream()
                        .map(ContactForSendDTO::getId)
                        .collect(Collectors.toList())
        );

        List<DocumentAttachment> newAttachmentsForDocument = new ArrayList<>();

        contactsForSendDto.forEach(contactForSend -> {
            Optional<Contact> contact = userContactsToReceive
                    .stream()
                    .filter(userContact -> userContact.getId().equals(contactForSend.getId()))
                    .findFirst();

            if (!contact.isPresent()) {
                throw new MailNotFoundException("Contact: " + contactForSend.getId() + " was not found");
            }

            DocumentAttachment attachment = new DocumentAttachment();

            attachment.setDocument(createDoc);
            attachment.setContact(contact.get());
            attachment.setSendBy(contactForSend.getSendBy());

            String documentUrl;

            try {
                this.fileUploaderAdapter.setDocumentsDestination(this.destinationForDocuments);
                documentUrl = this.fileUploaderAdapter.uploadDocument(
                        document.getOriginalFile(),
                        document.getFileName()
                );
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                throw new BadRequestException("There was an error uploading the document...");
            }

            if (attachment.getSendBy() == SendBy.EMAIL) {
                this.emailHandlerAdapter.sendDocumentMailMessage(
                        contact.get().getEmail(),
                        documentUrl
                );
            }

            newAttachmentsForDocument.add(attachment);
        });

        List<DocumentAttachment> newAttachments = this.documentAttachmentRepository.saveAll(
                newAttachmentsForDocument
        );

        createDoc.setDocumentAttachments(newAttachments);

        return createDoc;
    }

    private String randomFileName(String originalFileName) {
        return originalFileName + "_" + LocalDateTime.now()
                .toString()
                .replaceAll(":", "_")
                .concat(String.valueOf(LocalDateTime.now().getNano() * 10 ^ 5));
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
