package com.softwareprojekt.gruppe8.data;

import java.util.Optional;

import com.softwareprojekt.gruppe8.konto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);
}
