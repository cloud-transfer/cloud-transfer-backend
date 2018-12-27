package com.github.cloud_transfer.cloud_transfer_backend.model.token;


import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "token_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "tokens")
public abstract class Token {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_id_in_tokens"))
    private User user;

    protected abstract String getAccessToken();
}
