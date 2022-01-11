package com.example.restauracja.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dish_orders")
public class DishOrder {
  @Id
  private String id;
  private String dishId;
  private int quantity;

  public DishOrder(String dishId, int quantity) {
    this.dishId = dishId;
    this.quantity = quantity;
  }

  public String getDishId() {
    return dishId;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "DishOrder{" +
      ", dishId='" + dishId + '\'' +
      ", quantity=" + quantity +
      '}';
  }
}
