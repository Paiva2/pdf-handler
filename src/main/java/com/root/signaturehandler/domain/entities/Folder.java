package com.root.signaturehandler.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_folders", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fk_user", "name"})
})
public class Folder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at", updatable = false)
    private Date createdAt = new Date();

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user")
    private User user;

    @OneToMany(mappedBy = "folder", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Document> documents;
}
