package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.utils.EncrypterHandler;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.infra.repositories.UserRepository;
import com.root.signaturehandler.presentation.exceptions.ForbiddenException;
import com.root.signaturehandler.presentation.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
}
