package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.Folder;

import java.util.Optional;
import java.util.UUID;

public interface FolderModel {
    Optional<Folder> findUserFolder(UUID userId, String folderName);
}
