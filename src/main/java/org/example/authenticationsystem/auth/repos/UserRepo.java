package org.example.authenticationsystem.auth.repos;

import org.example.authenticationsystem.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
