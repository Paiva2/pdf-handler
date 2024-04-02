package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.domain.entities.Document;
import com.root.signaturehandler.domain.entities.DocumentAttachment;
import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.utils.EmailHandlerAdapter;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;
    private final ContactRepository contactRepository;
    private final DocumentAttachmentRepository documentAttachmentRepository;
    private final EmailHandlerAdapter emailHandlerAdapter;

    public DocumentService(
            FolderRepository folderRepository,
            DocumentRepository documentRepository,
            DocumentAttachmentRepository documentAttachmentRepository,
            ContactRepository contactRepository,
            EmailHandlerAdapter emailHandlerAdapter
    ) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.documentAttachmentRepository = documentAttachmentRepository;
        this.emailHandlerAdapter = emailHandlerAdapter;
        this.contactRepository = contactRepository;
    }

    @Transactional(noRollbackFor = MailNotFoundException.class)
    public Document sendDocument(
            UUID userId,
            Document document,
            HashSet<ContactForSendDTO> contactsForSendDto) {
        ArrayList<String> requiredFields = new ArrayList<String>() {{
            add("userId");
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

        List<Contact> userContactsToReceive = this.contactRepository.findContactsByIdByUserId(
                userId,
                contactsForSendDto
                        .stream()
                        .map(ContactForSendDTO::getId)
                        .collect(Collectors.toList())
        );

        Set<DocumentAttachment> newAttachmentsForDocument = new HashSet<>();

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

            if (attachment.getSendBy() == SendBy.EMAIL) {
                this.emailHandlerAdapter.sendDocumentMailMessage(
                        contact.get().getEmail(),
                        "https://linktos3bucketwithpdf.com.br" // TODO: IMPLEMENT S3 BUCKET TO UPLOAD DOC
                );
            }

            newAttachmentsForDocument.add(attachment);
        });

        this.documentAttachmentRepository.saveAll(newAttachmentsForDocument);

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
