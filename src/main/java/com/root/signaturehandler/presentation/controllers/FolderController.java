package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.domain.entities.Folder;
import com.root.signaturehandler.domain.services.FolderService;
import com.root.signaturehandler.presentation.dtos.in.folder.CreateFolderDTO;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
}
