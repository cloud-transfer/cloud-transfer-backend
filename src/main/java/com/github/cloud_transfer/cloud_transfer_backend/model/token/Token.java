package com.github.cloud_transfer.cloud_transfer_backend.model.token;


import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "token_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "tokens")
public abstract class Token {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_id_in_tokens"))
    private User user;

    protected abstract String getAccessToken();
}
