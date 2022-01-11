package com.example.restauracja.repositories;

import com.example.restauracja.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
  List<Order> findAllByUserId(String userId);
}
