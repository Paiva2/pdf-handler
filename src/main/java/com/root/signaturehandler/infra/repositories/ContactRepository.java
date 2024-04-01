package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.infra.models.ContactModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID>, ContactModel, JpaSpecificationExecutor<Contact> {

    @Override
    @Query(value = "SELECT contact.* FROM tb_contacts AS contact " +
            "WHERE contact.fk_user = :userId AND (contact.phone = :phone OR contact.email = :email)",
            nativeQuery = true
    )
    Optional<Contact> findUserContact(UUID userId, String email, String phone);
}
