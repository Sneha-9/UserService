package com.sneha.store;

import com.sneha.model.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDao, String> {
    Optional<UserDao> findByEmail(String email);

}
