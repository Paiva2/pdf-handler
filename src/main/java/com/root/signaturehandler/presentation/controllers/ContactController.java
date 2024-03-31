package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.domain.services.ContactService;
import com.root.signaturehandler.presentation.dtos.in.contact.RegisterContactDTO;
import com.root.signaturehandler.presentation.dtos.out.ContactResponseDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {
    private final ContactService contactService;
    private final JwtAdapter jwtAdapter;

    public ContactController(ContactService contactService, JwtAdapter jwtAdapter) {
        this.contactService = contactService;
        this.jwtAdapter = jwtAdapter;
    }

    @PostMapping("/new")
    public ResponseEntity<ContactResponseDTO> registerContact(
            @RequestBody RegisterContactDTO dto,
            @RequestHeader("Authorization") String authToken
    ) {
        String parseToken = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Contact newContact = this.contactService.registerNew(UUID.fromString(parseToken), dto.toContact());

        ContactResponseDTO contactResponseDTO = new ContactResponseDTO(
                newContact.getId(),
                newContact.getName(),
                newContact.getEmail(),
                newContact.getPhone(),
                newContact.getCreatedAt()
        );

        return ResponseEntity.status(201).body(contactResponseDTO);
    }
}