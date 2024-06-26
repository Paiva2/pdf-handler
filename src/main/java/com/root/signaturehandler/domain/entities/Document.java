package com.root.signaturehandler.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Transient
    @JsonIgnore
    private byte[] fileBinary;

    @Column(name = "document_url", nullable = false)
    private String documentUrl;

    @Column(nullable = false)
    private Boolean disabled = false;

    @CreatedDate
    @Column(name = "deleted_at", nullable = true)
    private Date deletedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_folder")
    private Folder folder;

    @JsonIgnore
    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<DocumentAttachment> documentAttachments;

    @Transient
    @JsonIgnore
    private MultipartFile originalFile;
}
