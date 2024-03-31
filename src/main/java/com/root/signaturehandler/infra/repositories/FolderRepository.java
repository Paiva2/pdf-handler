package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.infra.models.FolderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID>, FolderModel {
    @Query("SELECT fold FROM Folder fold" +
            " LEFT JOIN FETCH fold.user usr INNER JOIN FETCH fold.documents doc " +
            " WHERE fold.user.id = :userId AND fold.name = :folderName"
    )
    Optional<Folder> findUserFolderByName(UUID userId, String folderName);

    @Query("SELECT fold FROM Folder fold " +
            " LEFT JOIN FETCH fold.documents doc " +
            " WHERE fold.user.id = :userId AND fold.id = :folderId"
    )
    Optional<Folder> findUserFolderById(UUID userId, Long folderId);
}
