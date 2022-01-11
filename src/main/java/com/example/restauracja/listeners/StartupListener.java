package com.example.restauracja.listeners;

import com.example.restauracja.models.AppUser;
import com.example.restauracja.models.Dish;
import com.example.restauracja.repositories.DishRepository;
import com.example.restauracja.repositories.OrderRepository;
import com.example.restauracja.repositories.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupListener {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final DishRepository dishRepository;
  private final OrderRepository orderRepository;

  public StartupListener(UserRepository userRepository, DishRepository dishRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.dishRepository = dishRepository;
    this.passwordEncoder = passwordEncoder;
    this.orderRepository = orderRepository;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void runAfterStartup() {
//    this.setupUserRepository();
//    this.setupDishRepository();
//    this.setupOrderRepository();
  }

  private void setupUserRepository() {
//    this.userRepository.deleteAll();

    AppUser theManager = new AppUser("manager", this.passwordEncoder.encode("manager"), List.of("MANAGER"));
    AppUser theStaff = new AppUser("staff", this.passwordEncoder.encode("staff"), List.of("STAFF"));
    AppUser theClient = new AppUser("client", this.passwordEncoder.encode("client"), List.of("CLIENT"));

    this.userRepository.save(theManager);
    this.userRepository.save(theStaff);
    this.userRepository.save(theClient);
  }

  private void setupDishRepository() {
    this.dishRepository.deleteAll();

    this.dishRepository.save(new Dish("Pizza",6000));
    this.dishRepository.save(new Dish("Hamburger",3500));
    this.dishRepository.save(new Dish("Hot-dog",900));
    this.dishRepository.save(new Dish("Cola",600));
    this.dishRepository.save(new Dish("Fanta",500));
    this.dishRepository.save(new Dish("Sprite",700));
  }

  private void setupOrderRepository() {
//        this.orderRepository.deleteAll();
  }
}
