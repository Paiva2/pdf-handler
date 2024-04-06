package com.root.signaturehandler.presentation.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ListAllContactsResponseDTO {
    List<ContactResponseDTO> contactsList;
    long totalElements;
    Integer totalPages;
    Integer pageSize;
    Integer page;
}
