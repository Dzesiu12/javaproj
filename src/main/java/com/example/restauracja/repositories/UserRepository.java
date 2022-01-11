package com.example.restauracja.repositories;

import com.example.restauracja.models.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AppUser, String> {
  Optional<AppUser> findByUsername(String username);
}
