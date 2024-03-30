package com.root.signaturehandler.services;

import com.root.signaturehandler.entities.User;
import com.root.signaturehandler.exceptions.BadRequestException;
import com.root.signaturehandler.exceptions.ConflictException;
import com.root.signaturehandler.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User newUser) {
        if (newUser.getPassword().length() < 6) {
            throw new BadRequestException("Password can't be less than 6.");
        }

        Optional<User> doesUserAlreadyExists = this.userRepository.findByEmail(newUser.getEmail());

        if (doesUserAlreadyExists.isPresent()) {
            throw new ConflictException("E-mail already exists");
        }

        return this.userRepository.save(newUser);
    }
}
