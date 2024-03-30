package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.utils.EncrypterHandler;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.infra.repositories.UserRepository;
import com.root.signaturehandler.presentation.exceptions.ForbiddenException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EncrypterHandler encrypterHandler;

    public UserService(UserRepository userRepository, EncrypterHandler encrypterHandler) {
        this.userRepository = userRepository;
        this.encrypterHandler = encrypterHandler;
    }

    public User registerUser(User newUser) {
        if (newUser.getPassword().length() < 6) {
            throw new BadRequestException("Password can't be less than 6.");
        }

        Optional<User> doesUserAlreadyExists = this.userRepository.findByEmail(newUser.getEmail());

        if (doesUserAlreadyExists.isPresent()) {
            throw new ConflictException("E-mail already exists");
        }

        String hashedPassword = this.encrypterHandler.encrypt(newUser.getPassword());

        newUser.setPassword(hashedPassword);

        return this.userRepository.save(newUser);
    }

    public User authUser(User user) {
        Optional<User> doesUserExists = this.userRepository.findByEmail(user.getEmail());

        if (!doesUserExists.isPresent()) {
            throw new NotFoundException("User not found.");
        }

        boolean doesPasswordsMatches = this.encrypterHandler
                .compare(user.getPassword(), doesUserExists.get().getPassword());

        if (!doesPasswordsMatches) {
            throw new ForbiddenException("Wrong credentials.");
        }

        return doesUserExists.get();
    }

    public User getProfile(UUID userId) {
        if (userId == null) {
            throw new BadRequestException("userId can't be empty.");
        }

        Optional<User> doesUserExists = this.userRepository.findById(userId);

        if (!doesUserExists.isPresent()) {
            throw new NotFoundException("User not found");
        }

        return doesUserExists.get();
    }

    public User updateProfile(User updatedUser) {
        if (updatedUser == null) {
            throw new BadRequestException("updatedUser can't be null");
        }

        if (updatedUser.getId() == null) {
            throw new BadRequestException("updatedUser Id can't be null");
        }

        Optional<User> doesUserExists = this.userRepository.findById(updatedUser.getId());

        if (!doesUserExists.isPresent()) {
            throw new NotFoundException("User not found");
        }

        if (updatedUser.getPassword() != null) {
            if (updatedUser.getPassword().length() < 6) {
                throw new BadRequestException("password can't be less than 6 characters");
            } else {
                String hashNewPassword = this.encrypterHandler.encrypt(updatedUser.getPassword());

                updatedUser.setPassword(hashNewPassword);
            }
        }

        if (updatedUser.getEmail() != null) {
            Optional<User> doesEmailIsAlreadyRegistered = this.userRepository.findByEmail(updatedUser.getEmail());

            if (doesEmailIsAlreadyRegistered.isPresent()) {
                User existentEmail = doesEmailIsAlreadyRegistered.get();

                if (existentEmail.getId().hashCode() != updatedUser.getId().hashCode()) {
                    throw new ConflictException("Provided new e-mail already exists");
                }
            }
        }

        BeanWrapper target = new BeanWrapperImpl(doesUserExists.get());
        BeanWrapper sourceUpdated = new BeanWrapperImpl(updatedUser);

        PropertyDescriptor[] fields = sourceUpdated.getPropertyDescriptors();

        for (PropertyDescriptor field : fields) {
            String fieldName = field.getName();
            Object newValue = sourceUpdated.getPropertyValue(fieldName);

            boolean nonUpdatableFields =
                    fieldName.hashCode() == "class".hashCode() || fieldName.hashCode() == "id".hashCode();

            if (!nonUpdatableFields && newValue != null) {
                target.setPropertyValue(fieldName, newValue);
            }
        }

        return this.userRepository.save(doesUserExists.get());
    }
}
