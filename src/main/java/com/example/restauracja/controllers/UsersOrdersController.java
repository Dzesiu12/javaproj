package com.example.restauracja.controllers;

import com.example.restauracja.enums.ResponseType;
import com.example.restauracja.models.Dish;
import com.example.restauracja.models.DishOrder;
import com.example.restauracja.models.Order;
import com.example.restauracja.models.Response;
import com.example.restauracja.repositories.DishOrderRepository;
import com.example.restauracja.repositories.DishRepository;
import com.example.restauracja.repositories.OrderRepository;
import com.example.restauracja.requests.CreateOrderRequest;
import com.example.restauracja.utils.JwtUtils;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders/users")
public class UsersOrdersController {
  OrderRepository orderRepository;
  DishOrderRepository dishOrderRepository;
  DishRepository dishRepository;

  public UsersOrdersController(OrderRepository orderRepository, DishOrderRepository dishOrderRepository, DishRepository dishRepository) {
    this.orderRepository = orderRepository;
    this.dishOrderRepository = dishOrderRepository;
    this.dishRepository = dishRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getOrders(@RequestHeader("Authorization") String authHeader) {
    List<Order> orders = orderRepository.findAllByUserId(JwtUtils.getUserIdFromHeader(authHeader));

    Document response = new Document();
    List<Document> ordersList = new ArrayList<>();

    for (Order order : orders) {
      Document orderDocument = new Document();
      orderDocument.append("id", order.getId());
      orderDocument.append("isCompleted", order.isCompleted());
      int orderPrice = 0;

      List<Document> dishesList = new ArrayList<>();
      for (String orderDishId : order.getOrderDishesIds()) {
        Document dishDocument = new Document();
        DishOrder dishOrder = dishOrderRepository.findById(orderDishId).get();
        Dish dish = dishRepository.findById(dishOrder.getDishId()).get();
        dishDocument.append("id", dish.getId());
        dishDocument.append("name", dish.getName());
        dishDocument.append("quantity", dishOrder.getQuantity());
        orderPrice += dish.getPrice() * dishOrder.getQuantity();
        dishesList.add(dishDocument);
      }
      orderDocument.append("price", orderPrice);
      orderDocument.append("dishes", dishesList);
      ordersList.add(orderDocument);
    }

    response.append("orders", ordersList);


    try {
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, null, response), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createOrder(@RequestHeader("Authorization") String authHeader, @RequestBody CreateOrderRequest request) {
    String userId = JwtUtils.getUserIdFromHeader(authHeader);

    Order order = new Order(userId);
    for (DishOrder d : request.dishes) {
      DishOrder dishOrder = this.dishOrderRepository.save(new DishOrder(d.getDishId(), d.getQuantity()));
      order.addOrderDish(dishOrder.getId());
    }

    this.orderRepository.save(order);

    try {
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, "Order created"), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }
  }
}
