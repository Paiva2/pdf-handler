package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.utils.ClassPropertiesAdapter;
import com.root.signaturehandler.infra.repositories.FolderRepository;
import com.root.signaturehandler.infra.repositories.UserRepository;
import com.root.signaturehandler.infra.specifications.FolderSpecification;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FolderSpecification folderSpecification;

    public FolderService(UserRepository userRepository, FolderRepository folderRepository, FolderSpecification folderSpecification) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.folderSpecification = folderSpecification;
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
                this.folderRepository.findUserFolderByName(folderOwnerId, newFolder.getName());

        if (doesFolderExists.isPresent()) {
            throw new ConflictException("User already has a folder with this name");
        }

        newFolder.setUser(user);

        return this.folderRepository.save(newFolder);
    }

    public Folder filterFolder(UUID userId, Long folderId) {
        if (userId == null) {
            throw new BadRequestException("userId can't be null");
        }

        if (folderId == null) {
            throw new BadRequestException("folderId can't be null");
        }

        Optional<Folder> doesUserHasFolder =
                this.folderRepository.findUserFolderById(userId, folderId);

        if (!doesUserHasFolder.isPresent()) {
            throw new NotFoundException("Folder not found");
        }

        return doesUserHasFolder.get();
    }

    public Folder editFolder(UUID userId, Long folderId, Folder folderUpdate) {
        if (userId == null) {
            throw new BadRequestException("userId can't be empty");
        }

        if (folderId == null) {
            throw new BadRequestException("folderId can't be empty");
        }

        if (folderUpdate == null) {
            throw new BadRequestException("folderId can't be empty");
        }

        Optional<Folder> doesFolderExists = this.folderRepository.findUserFolderById(
                userId,
                folderId
        );

        if (!doesFolderExists.isPresent()) {
            throw new NotFoundException("Folder not found");
        }

        new ClassPropertiesAdapter<>(doesFolderExists.get(), folderUpdate).copyNonNullProperties();

        Folder folderUpdated = this.folderRepository.save(doesFolderExists.get());

        return folderUpdated;
    }

    public Page<Folder> listFolders(UUID userId, String folderName, int page, int perPage) {
        if (userId == null) {
            throw new BadRequestException("userId can't be empty");
        }

        if (page < 0) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.ASC, "createdAt");

        Specification<Folder> filters = Specification.where(this.folderSpecification.userIdEq(userId))
                .and(folderName != null ? this.folderSpecification.folderNameLike(folderName) : null);

        Page<Folder> folders = this.folderRepository.findAll(filters, pageable);

        return folders;
    }
}
