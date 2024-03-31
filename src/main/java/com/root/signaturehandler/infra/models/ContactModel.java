package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Contact;

import java.util.Optional;
import java.util.UUID;

public interface ContactModel {
    //Contact save(Contact contact);

    Optional<Contact> findUserContact(UUID userId, String email, String phone);
}
