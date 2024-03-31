package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.infra.repositories.FolderRepository;
import com.root.signaturehandler.infra.repositories.UserRepository;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public FolderService(UserRepository userRepository, FolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    public Folder createFolder(UUID folderOwnerId, Folder newFolder) {
        if (folderOwnerId == null) {
            throw new BadRequestException("folderOwnerId can't be null");
        }

        if (newFolder == null) {
            throw new BadRequestException("newFolder can't be null");
        }

        if (newFolder.getName() == null) {
            throw new BadRequestException("folderName can't be null");
        }

        Optional<User> doesUserExists = this.userRepository.findById(folderOwnerId);

        if (!doesUserExists.isPresent()) {
            throw new NotFoundException("User not found");
        }

        User user = doesUserExists.get();

        Optional<Folder> doesFolderExists =
                this.folderRepository.findUserFolder(user.getId(), newFolder.getName());

        if (doesFolderExists.isPresent()) {
            throw new ConflictException("User already has a folder with this name");
        }

        newFolder.setUser(user);

        return this.folderRepository.save(newFolder);
    }
}
