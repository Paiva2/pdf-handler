package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.utils.ClassPropertiesAdapter;
import com.root.signaturehandler.infra.repositories.ContactRepository;
import com.root.signaturehandler.infra.repositories.UserRepository;
import com.root.signaturehandler.infra.specifications.ContactSpecification;
import com.root.signaturehandler.presentation.dtos.in.contact.EditContactDTO;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.presentation.exceptions.ForbiddenException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactSpecification contactSpecification;

    public ContactService(
            ContactRepository contactRepository,
            UserRepository userRepository,
            ContactSpecification contactSpecification
    ) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.contactSpecification = contactSpecification;
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

    public Page<Contact> filterUserContacts(
            UUID userId,
            int page,
            int size,
            String email,
            String name
    ) {
        if (userId == null) {
            throw new BadRequestException("userId can't be null");
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 100) {
            size = 100;
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        ContactSpecification specification = this.contactSpecification;

        Specification<Contact> filters =
                Specification.where(specification.userIdEq(userId))
                        .and(name != null ? specification.nameLike(name) :
                                email != null ? specification.emailLike(email) : null);

        Page<Contact> getContacts = this.contactRepository.findAll(filters, pageable);

        return getContacts;
    }

    public Contact editContact(UUID userId, UUID contactId, EditContactDTO editContactDTO) {
        if (userId == null) {
            throw new BadRequestException("userId can't be empty");
        }

        if (editContactDTO == null) {
            throw new BadRequestException("editContactDTO can't be empty");
        }

        Optional<Contact> doesContactExists = this.contactRepository.findById(
                contactId
        );

        if (!doesContactExists.isPresent()) {
            throw new NotFoundException("Contact not found");
        }

        Contact contact = doesContactExists.get();

        if (!contact.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Only contact owners can delete their own contacts");
        }

        new ClassPropertiesAdapter<>(contact, editContactDTO).copyNonNullProperties();

        Contact updatedContact = this.contactRepository.save(contact);

        return updatedContact;
    }

    @Transactional
    public void removeContact(UUID userId, UUID contactId) {
        if (userId == null) {
            throw new BadRequestException("userId can't be empty");
        }

        if (contactId == null) {
            throw new BadRequestException("userId can't be empty");
        }

        Contact removedContact = this.contactRepository.deleteByIdModel(contactId)
                .orElseThrow(() -> new NotFoundException("Contact not found"));

        if (!removedContact.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Only contact owners can remove their contacts");
        }
    }

    private boolean phoneValidation(String phone) {
        String phoneRegexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";

        return Pattern.matches(phoneRegexp, phone);
    }
}
