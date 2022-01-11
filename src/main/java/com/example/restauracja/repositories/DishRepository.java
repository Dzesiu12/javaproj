package com.example.restauracja.repositories;

import com.example.restauracja.models.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DishRepository extends MongoRepository<Dish, String> {
}
