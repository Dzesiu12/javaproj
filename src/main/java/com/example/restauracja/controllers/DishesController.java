package com.example.restauracja.controllers;

import com.example.restauracja.enums.ResponseType;
import com.example.restauracja.models.Dish;
import com.example.restauracja.models.Response;
import com.example.restauracja.repositories.DishRepository;
import com.example.restauracja.requests.CreateDishRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dishes")
public class DishesController {
  private final DishRepository dishRepository;

  public DishesController(DishRepository dishRepository) {
    this.dishRepository = dishRepository;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createDish(@RequestBody CreateDishRequest request) {
    try {
      Dish dish = new Dish(request.name, request.price);
      this.dishRepository.save(dish);
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, "Dish added", dish), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getDishes() {
    try {
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, null, dishRepository.findAll()), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }

  }

  @DeleteMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createDish(@PathVariable String id) {
    try {
      Dish dish = this.dishRepository.findById(id).orElse(null);
      if (dish != null) {
        dishRepository.deleteById(id);
        return new ResponseEntity<>(new Response(ResponseType.SUCCESS, "Dish deleted", dish), HttpStatus.OK);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(new Response(ResponseType.ERROR, "Dish not found"), HttpStatus.NOT_FOUND);
  }
}
