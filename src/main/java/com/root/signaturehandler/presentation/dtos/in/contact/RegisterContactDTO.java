package com.root.signaturehandler.presentation.dtos.in.contact;

import com.root.signaturehandler.domain.entities.Contact;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterContactDTO {
    @NotBlank(message = "name can't be empty")
    @NotNull(message = "name can't be null")
    private String name;

    @NotBlank(message = "email can't be empty")
    @NotNull(message = "email can't be null")
    private String email;

    @NotBlank(message = "phone can't be empty")
    @NotNull(message = "phone can't be null")
    @Pattern(regexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$")
    private String phone;

    public Contact toContact() {
        Contact contact = new Contact();
        contact.setName(this.name);
        contact.setEmail(this.email);
        contact.setPhone(this.phone);

        return contact;
    }
}
