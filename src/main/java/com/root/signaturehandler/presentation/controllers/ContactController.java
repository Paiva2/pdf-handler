package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.domain.services.ContactService;
import com.root.signaturehandler.presentation.dtos.in.contact.RegisterContactDTO;
import com.root.signaturehandler.presentation.dtos.out.ContactResponseDTO;
import com.root.signaturehandler.presentation.dtos.out.ListAllContactsResponseDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<ContactResponseDTO> registerContact(@RequestBody RegisterContactDTO dto,
                                                              @RequestHeader("Authorization") String authToken) {
        String parseToken = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Contact newContact =
                this.contactService.registerNew(UUID.fromString(parseToken), dto.toContact());

        ContactResponseDTO contactResponseDTO =
                new ContactResponseDTO(newContact.getId(), newContact.getName(),
                        newContact.getEmail(), newContact.getPhone(), newContact.getCreatedAt().toString());

        return ResponseEntity.status(201).body(contactResponseDTO);
    }

    @GetMapping("/list-all")
    public ResponseEntity<ListAllContactsResponseDTO> filterUserContacts(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email) throws NumberFormatException {
        String parseTokenSubject = this.jwtAdapter.verify(authToken.replace("Bearer", ""));

        Page<Contact> contactsPageable = this.contactService
                .filterUserContacts(UUID.fromString(parseTokenSubject), page, size, email, name);

        List<ContactResponseDTO> contactResponseDTOList = contactsPageable.getContent().stream().map(contact -> {
            return new ContactResponseDTO(
                    contact.getId(),
                    contact.getName(),
                    contact.getEmail(),
                    contact.getPhone(),
                    contact.getCreatedAt().toString()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.status(200).body(new ListAllContactsResponseDTO(
                contactResponseDTOList,
                contactsPageable.getTotalElements(),
                contactsPageable.getTotalPages(),
                contactsPageable.getSize(),
                contactsPageable.getNumber()
        ));
    }

    @DeleteMapping("{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable(name = "contactId") UUID contactId,
                                              @RequestHeader("Authorization") String authToken) {
        String parseTokenSubject = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        this.contactService.removeContact(UUID.fromString(parseTokenSubject), contactId);

        return ResponseEntity.status(204).build();
    }
}
