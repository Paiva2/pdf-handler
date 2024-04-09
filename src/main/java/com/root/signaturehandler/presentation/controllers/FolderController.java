package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.services.FolderService;
import com.root.signaturehandler.presentation.dtos.in.folder.CreateFolderDTO;
import com.root.signaturehandler.presentation.dtos.in.folder.UpdateFolderDTO;
import com.root.signaturehandler.presentation.dtos.out.AllFoldersNoDocsResponseDTO;
import com.root.signaturehandler.presentation.dtos.out.FolderNoDocsResponseDTO;
import com.root.signaturehandler.presentation.dtos.out.FolderResponseDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/folder")
public class FolderController {
    private final FolderService folderService;
    private final JwtAdapter jwtAdapter;

    public FolderController(FolderService folderService, JwtAdapter jwtAdapter) {
        this.folderService = folderService;
        this.jwtAdapter = jwtAdapter;
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> newFolder(
            @RequestBody @Valid CreateFolderDTO dto,
            @RequestHeader("Authorization") String authToken
    ) {
        String parseSubjectToken = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Folder folderCreated = this.folderService.createFolder(UUID.fromString(parseSubjectToken), dto.toFolder());

        Map<String, Object> responseMap = new LinkedHashMap<String, Object>() {{
            put("message", "Folder created successfully!");
            put("newFolderId", folderCreated.getId());
            put("newFolderName", folderCreated.getName());
        }};

        return ResponseEntity.status(201).body(responseMap);
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<FolderResponseDTO> filterFolderById(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(name = "folderId") Long folderId
    ) {
        String parseTokenSubject = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        Folder getFolder = this.folderService.filterFolder(UUID.fromString(parseTokenSubject), folderId);

        FolderResponseDTO folderResponseDTO = new FolderResponseDTO(
                getFolder.getId(),
                getFolder.getName(),
                getFolder.getCreatedAt(),
                getFolder.getDocuments()
        );

        return ResponseEntity.status(200).body(folderResponseDTO);
    }

    @PatchMapping("/update/{folderId}")
    public ResponseEntity<FolderNoDocsResponseDTO> updateFolder(
            @RequestHeader(name = "Authorization") String authToken,
            @PathVariable(name = "folderId") Long folderId,
            @RequestBody @Valid UpdateFolderDTO updateFolderDto
    ) {
        String parseTokenSub = this.jwtAdapter.verify(authToken.replace("Bearer", ""));

        Folder folder = new Folder();
        folder.setName(updateFolderDto.getName());

        Folder updatedFolder = this.folderService.editFolder(
                UUID.fromString(parseTokenSub),
                folderId,
                folder
        );

        FolderNoDocsResponseDTO folderResponseDTO = new FolderNoDocsResponseDTO(
                updatedFolder.getId(),
                updatedFolder.getName(),
                updatedFolder.getCreatedAt().toString()
        );

        return ResponseEntity.status(201).body(folderResponseDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<AllFoldersNoDocsResponseDTO> listAllFolders(
            @RequestHeader(name = "Authorization") String authToken,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int perPage,
            @RequestParam(name = "name", required = false) String folderName
    ) {
        String parseTokenSub = this.jwtAdapter.verify(authToken.replace("Bearer", ""));

        Page<Folder> folderPage = this.folderService.listFolders(
                UUID.fromString(parseTokenSub),
                folderName,
                page,
                perPage
        );

        AllFoldersNoDocsResponseDTO listAllContactsResponseDTO = new AllFoldersNoDocsResponseDTO(
                folderPage.getContent().stream().map(folder ->
                        new FolderNoDocsResponseDTO(
                                folder.getId(),
                                folder.getName(),
                                folder.getCreatedAt().toString()
                        )
                ).collect(Collectors.toList()),
                folderPage.getTotalElements(),
                folderPage.getTotalPages(),
                folderPage.getSize(),
                folderPage.getNumber() + 1
        );


        return ResponseEntity.status(200).body(listAllContactsResponseDTO);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @RequestHeader("Authorization") String authToken,
            @PathVariable("folderId") String folderId
    ) {
        String parseTokenSub = this.jwtAdapter.verify(authToken.replace("Bearer ", ""));

        this.folderService.deleteFolder(
                UUID.fromString(parseTokenSub),
                Long.valueOf(folderId)
        );

        return ResponseEntity.status(204).build();
    }
}
