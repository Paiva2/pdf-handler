package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Folder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderModel {
    Optional<Folder> findUserFolderByName(UUID userId, String folderName);

    Optional<Folder> findUserFolderById(UUID userId, Long folderId);
}
