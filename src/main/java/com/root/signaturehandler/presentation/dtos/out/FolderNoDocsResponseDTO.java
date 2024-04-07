package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FolderNoDocsResponseDTO {
    private Long id;
    private String name;
    private String createdAt;
}
