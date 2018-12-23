package com.github.cloud_transfer.cloud_transfer_backend.repository;

import com.github.cloud_transfer.cloud_transfer_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);

}
