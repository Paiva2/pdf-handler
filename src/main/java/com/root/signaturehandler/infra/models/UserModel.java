package com.root.signaturehandler.infra.models;

import com.root.signaturehandler.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserModel {
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    //User save(User id);
}
