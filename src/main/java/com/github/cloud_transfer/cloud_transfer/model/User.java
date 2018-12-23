package com.github.cloud_transfer.cloud_transfer.model;

import com.github.cloud_transfer.cloud_transfer.model.token.Token;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"email"},
                name = "uk_email"
        )
})
@EqualsAndHashCode(exclude = {"id"})
public class User {

    @Id
    private UUID id;

    private String name;

    @Column(nullable = false)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Token> tokens = new HashSet<>();
}
