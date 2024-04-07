package com.root.signaturehandler.presentation.dtos.in.folder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateFolderDTO {
    @NotEmpty(message = "name can't be empty")
    @NotNull(message = "name can't be null")
    private String name;
}
