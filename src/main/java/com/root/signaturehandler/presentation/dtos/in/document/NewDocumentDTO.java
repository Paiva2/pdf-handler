package com.root.signaturehandler.presentation.dtos.in.document;

import com.root.signaturehandler.presentation.dtos.in.contact.ContactForSendDTO;
import lombok.Data;

import java.util.HashSet;

@Data
public class NewDocumentDTO {
    HashSet<ContactForSendDTO> contactsForSend;
}
