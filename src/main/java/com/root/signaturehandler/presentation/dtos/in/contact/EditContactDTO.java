package com.root.signaturehandler.presentation.dtos.in.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditContactDTO {
    @NotBlank(message = "name can't be blank")
    @NotNull(message = "name can't be null")
    private String name;

    @Email(message = "email must be an valid e-mail")
    @NotBlank(message = "email can't be blank")
    @NotNull(message = "email can't be null")
    private String email;

    @Pattern(regexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$")
    @NotNull(message = "phone can't be null")
    private String phone;
}
