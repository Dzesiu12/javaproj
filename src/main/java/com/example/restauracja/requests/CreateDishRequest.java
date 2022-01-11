package com.example.restauracja.requests;

public class CreateDishRequest {
  public String name;
  public int price;

  @Override
  public String toString() {
    return "CreateDishRequest{" +
            "name='" + name + '\'' +
            ", price=" + price +
            '}';
  }

}
