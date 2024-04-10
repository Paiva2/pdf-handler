package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllDocumentsDTO {
    List<DocumentResponseDTO> documentsList;
    long totalElements;
    Integer totalPages;
    Integer pageSize;
    Integer page;
}
