package com.example.restauracja.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dishes")
public class Dish {
  @Id
  private String id;
  private String name;
  private int price;


  public Dish(String name, int price ) {
    this.name = name;
    this.price = price;
  }

  public int getPrice() {
    return price;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

}
