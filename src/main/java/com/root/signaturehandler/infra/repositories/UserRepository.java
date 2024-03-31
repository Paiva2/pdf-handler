package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.infra.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends UserModel, JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM tb_users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM tb_users WHERE id = :id", nativeQuery = true)
    Optional<User> findById(UUID id);
}
