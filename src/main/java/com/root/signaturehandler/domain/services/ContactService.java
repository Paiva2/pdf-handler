package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.infra.repositories.ContactRepository;
import com.root.signaturehandler.infra.repositories.UserRepository;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public Contact registerNew(UUID userId, Contact contact) {
        if (userId == null) {
            throw new BadRequestException("userId can't be null");
        }

        if (contact == null) {
            throw new BadRequestException("contact can't be null");
        }

        if (!this.phoneValidation(contact.getPhone())) {
            throw new BadRequestException("Invalid phone");
        }

        Optional<User> doesUserExists = this.userRepository.findById(userId);

        if (!doesUserExists.isPresent()) {
            throw new NotFoundException("User not found");
        }

        Optional<Contact> doesUserAlreadyHasThisContact =
                this.contactRepository.findUserContact(userId, contact.getEmail(), contact.getPhone());

        if (doesUserAlreadyHasThisContact.isPresent()) {
            throw new ConflictException("User already has this contact on contact list");
        }

        contact.setUser(doesUserExists.get());

        return this.contactRepository.save(contact);
    }

    private boolean phoneValidation(String phone) {
        String phoneRegexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";

        return Pattern.matches(phoneRegexp, phone);
    }
}
