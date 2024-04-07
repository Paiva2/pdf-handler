package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderModel {
    Optional<Folder> findUserFolderByName(UUID userId, String folderName);

    Optional<Folder> findUserFolderById(UUID userId, Long folderId);

    Optional<Folder> findById(Long id);

    Page<Folder> findAll(Specification<Folder> specification, Pageable pageable);
}
