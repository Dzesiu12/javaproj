package com.example.restauracja.requests;

import com.example.restauracja.models.DishOrder;

import java.util.List;

public class CreateOrderRequest {
  public List<DishOrder> dishes;

  @Override
  public String toString() {
    return "CreateOrderRequest{" +
      "dishes=" + dishes +
      '}';
  }
}
