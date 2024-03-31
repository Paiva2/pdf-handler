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
    @Query(value = "SELECT * FROM tb_folders WHERE fk_user = :userId AND name = :folderName", nativeQuery = true)
    Optional<Folder> findUserFolder(UUID userId, String folderName);
}
