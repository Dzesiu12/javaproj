package com.example.restauracja.controllers;

import com.example.restauracja.enums.ResponseType;
import com.example.restauracja.models.Dish;
import com.example.restauracja.models.DishOrder;
import com.example.restauracja.models.Order;
import com.example.restauracja.models.Response;
import com.example.restauracja.repositories.DishOrderRepository;
import com.example.restauracja.repositories.DishRepository;
import com.example.restauracja.repositories.OrderRepository;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders/staff")
public class StaffOrdersController {
  private final DishRepository dishRepository;
  private final OrderRepository orderRepository;
  private final DishOrderRepository dishOrderRepository;

  public StaffOrdersController(OrderRepository orderRepository, DishOrderRepository dishOrderRepository, DishRepository dishRepository) {
    this.orderRepository = orderRepository;
    this.dishOrderRepository = dishOrderRepository;
    this.dishRepository = dishRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getOrders() {
    List<Order> orders = orderRepository.findAll();

    Document response = new Document();
    List<Document> ordersList = new ArrayList<>();

    for (Order order : orders) {
      Document orderDocument = new Document();
      orderDocument.append("id", order.getId());
      orderDocument.append("isCompleted", order.isCompleted());
      ordersList.add(orderDocument);

      List<Document> dishesList = new ArrayList<>();
      for (String orderDishId : order.getOrderDishesIds()) {
        Document dishDocument = new Document();
        DishOrder dishOrder = dishOrderRepository.findById(orderDishId).get();
        Dish dish = dishRepository.findById(dishOrder.getDishId()).get();
        dishDocument.append("id", dish.getId());
        dishDocument.append("name", dish.getName());
        dishDocument.append("quantity", dishOrder.getQuantity());
        dishesList.add(dishDocument);
      }

      orderDocument.append("dishes", dishesList);
    }

    response.append("orders", ordersList);

    try {
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, null, response), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/complete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> markAsCompleted(@PathVariable String id) {
    Order order = orderRepository.findById(id).orElse(null);
    if (order != null) {
      order.setCompleted(true);
      orderRepository.save(order);
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, "Order completed", order), HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(ResponseType.ERROR, "Order not found"), HttpStatus.NOT_FOUND);
  }
}
