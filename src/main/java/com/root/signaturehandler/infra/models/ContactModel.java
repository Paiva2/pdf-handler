package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface ContactModel {
    //Contact save(Contact contact);

    Optional<Contact> findUserContact(UUID userId, String email, String phone);

    //Page<Contact> findAll(Specification<Contact> filter, Pageable pageable);
}
