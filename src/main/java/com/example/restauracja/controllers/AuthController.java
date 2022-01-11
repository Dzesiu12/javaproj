package com.example.restauracja.controllers;

import com.example.restauracja.enums.ResponseType;
import com.example.restauracja.models.AppUser;
import com.example.restauracja.models.Response;
import com.example.restauracja.repositories.UserRepository;
import com.example.restauracja.requests.LoginForm;
import com.example.restauracja.requests.RegisterForm;
import com.example.restauracja.utils.JwtUtils;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> login(@RequestBody LoginForm loginForm) {
    AppUser existingUser = userRepository.findByUsername(loginForm.username).orElse(null);

    if (existingUser != null) {
      if (!this.passwordEncoder.matches(loginForm.password, existingUser.getPassword())) {
        return new ResponseEntity<>(new Response(ResponseType.ERROR, "Wrong username or password"), HttpStatus.UNAUTHORIZED);
      }

      Document response = new Document();
      response.append("token", JwtUtils.getTokenFromUser(existingUser));
      response.append("username", existingUser.getUsername());
      response.append("roles", existingUser.getAuthorities().stream().map(GrantedAuthority::getAuthority));
      try {
        return new ResponseEntity<>(new Response(ResponseType.SUCCESS, "Login successful", response), HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
      }
    }

    return new ResponseEntity<>(new Response(ResponseType.ERROR, "Wrong username or password"), HttpStatus.UNAUTHORIZED);
  }

  @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> register(@RequestBody RegisterForm registerForm) {
    if (userRepository.findByUsername(registerForm.username).isPresent()) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "User already exists"), HttpStatus.CONFLICT);
    }

    AppUser user = new AppUser(registerForm.username, this.passwordEncoder.encode(registerForm.password), List.of("CLIENT"));
    userRepository.save(user);

    Document response = new Document();
    response.append("token", JwtUtils.getTokenFromUser(user));
    response.append("username", user.getUsername());
    response.append("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority));
    try {
      return new ResponseEntity<>(new Response(ResponseType.SUCCESS, "Registered successfully", response), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new Response(ResponseType.ERROR, "Sf went wrong"), HttpStatus.BAD_REQUEST);
    }

  }
}
