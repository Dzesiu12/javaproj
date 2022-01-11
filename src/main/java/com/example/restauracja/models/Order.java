package com.example.restauracja.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
public class Order {
  @Id
  private String id;
  private String userId;
  private boolean isCompleted;
  private List<String> orderDishesIds;

  public Order(String userId) {
    this.userId = userId;
    this.isCompleted = false;
    this.orderDishesIds = new ArrayList<>();
  }

  public String getUserId() {
    return userId;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean completed) {
    isCompleted = completed;
  }

  public void addOrderDish(String orderDishId) {
    orderDishesIds.add(orderDishId);
  }

  public String getId() {
    return id;
  }

  public List<String> getOrderDishesIds() {
    return orderDishesIds;
  }

  @Override
  public String toString() {
    return "Order{" +
      "id='" + id + '\'' +
      ", userId='" + userId + '\'' +
      ", isCompleted=" + isCompleted +
      ", orderDishesIds=" + orderDishesIds +
      '}';
  }
}
