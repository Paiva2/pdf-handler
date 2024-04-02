package com.root.signaturehandler.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.root.signaturehandler.infra.models.enums.SendBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_document_attachments")
public class DocumentAttachment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();

    @Column(name = "send_by", nullable = false)
    @Enumerated(EnumType.STRING)
    private SendBy sendBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_document_id")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_contact_id")
    private Contact contact;
}
