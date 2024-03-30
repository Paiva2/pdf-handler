package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT * FROM tb_users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
    
}
