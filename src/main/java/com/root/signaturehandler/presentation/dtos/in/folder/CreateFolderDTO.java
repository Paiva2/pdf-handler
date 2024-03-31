package com.root.signaturehandler.presentation.dtos.in.folder;

import com.root.signaturehandler.domain.entities.Folder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateFolderDTO {
    @NotNull(message = "name can't be null")
    @NotEmpty(message = "name can't be empty")
    private String name;

    public Folder toFolder() {
        Folder folder = new Folder();
        folder.setName(this.name);

        return folder;
    }
}
