package com.example.restauracja.repositories;

import com.example.restauracja.models.DishOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DishOrderRepository extends MongoRepository<DishOrder, String> {
}
