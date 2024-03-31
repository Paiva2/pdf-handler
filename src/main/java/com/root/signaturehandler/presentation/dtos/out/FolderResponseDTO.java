package com.root.signaturehandler.presentation.dtos.out;

import com.root.signaturehandler.domain.entities.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FolderResponseDTO {
    private Long id;
    private String name;
    private Date createdAt;
    List<Document> documents;
}
