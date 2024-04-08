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
    @Size(min = 3, message = "Contact name must have at least 3 characters")
    private String name;

    @Email(message = "Email must be an valid e-mail")
    private String email;

    @Pattern(regexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$")
    private String phone;
}
