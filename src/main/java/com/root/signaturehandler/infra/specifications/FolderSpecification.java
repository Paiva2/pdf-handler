package com.root.signaturehandler.infra.specifications;

import com.root.signaturehandler.domain.entities.Folder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FolderSpecification {
    public Specification<Folder> userIdEq(UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("user").get("id"), userId);
    }

    public Specification<Folder> folderNameLike(String nameLike) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + nameLike + "%");
    }
}
