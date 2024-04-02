package com.root.signaturehandler.presentation.dtos.in.contact;

import com.root.signaturehandler.infra.models.enums.SendBy;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class ContactForSendDTO {
    @NotNull(message = "id can't be null")
    @NotEmpty(message = "id can't be empty")
    private UUID id;

    @NotNull(message = "email can't be null")
    @NotEmpty(message = "email can't be empty")
    @Email(message = "email must be an valid e-mail")
    private String email;

    @NotNull(message = "phone can't be null")
    @NotEmpty(message = "phone can't be empty")
    @Pattern(regexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$")
    private String phone;

    @NotNull(message = "sendBy can't be null")
    @NotEmpty(message = "sendBy can't be empty")
    private SendBy sendBy;
}
