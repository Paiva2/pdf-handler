package com.root.signaturehandler.infra.repositories;

import com.root.signaturehandler.domain.entities.Contact;
import com.root.signaturehandler.infra.models.ContactModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    @Query(value = "SELECT * FROM tb_contacts WHERE fk_user = :userId AND id IN (:contactIds)", nativeQuery = true)
    List<Contact> findContactsByIdByUserId(UUID userId, List<UUID> contactIds);

    @Override
    @Query(value = "SELECT c FROM Contact c INNER JOIN FETCH c.user u WHERE c.id = :contactId AND u.id = :userId")
    Optional<Contact> findByUserId(UUID contactId, UUID userId);

    @Override
    @Query(value = "DELETE FROM tb_contacts WHERE id = :contactId RETURNING *", nativeQuery = true)
    Optional<Contact> deleteByIdModel(UUID contactId);
}
