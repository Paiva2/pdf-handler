package com.root.signaturehandler.infra.specifications;

import com.root.signaturehandler.domain.entities.Document;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DocumentSpecification {
    DocumentSpecification() {
    }

    public Specification<Document> userIdLike(UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("folder").get("user").get("id"), userId);
    }

    public Specification<Document> nameLike(String name) {
        return (root, query, builder) -> builder.like(root.get("fileName"), "%" + name + "%");
    }

    public Specification<Document> isDisabled(boolean isDisabled) {
        return (root, query, builder) -> builder.equal(root.get("disabled"), isDisabled);
    }
}
