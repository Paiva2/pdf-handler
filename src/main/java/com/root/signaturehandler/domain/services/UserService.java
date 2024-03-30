package com.root.signaturehandler.domain.services;

import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.utils.EncrypterHandler;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import com.root.signaturehandler.presentation.exceptions.ConflictException;
import com.root.signaturehandler.infra.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
