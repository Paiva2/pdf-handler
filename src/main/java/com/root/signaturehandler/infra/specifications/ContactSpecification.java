package com.root.signaturehandler.infra.specifications;

import com.root.signaturehandler.domain.entities.Contact;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ContactSpecification {
    public ContactSpecification() {
    }

    public Specification<Contact> userIdEq(UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("user").get("id"), userId);
    }

    public Specification<Contact> nameLike(String nameLike) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + nameLike + "%");
    }

    public Specification<Contact> emailLike(String emailLike) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + emailLike + "%");
    }

    public Specification<Contact> phoneEq(String phoneEq) {
        return (root, query, builder) -> builder.equal(root.get("phone"), phoneEq);
    }

    public Specification<Contact> emailEq(String emailEq) {
        return (root, query, builder) -> builder.equal(root.get("email"), emailEq);
    }
}
