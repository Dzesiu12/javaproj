package com.example.restauracja.requests;

public class RegisterForm {
  public String username;
  public String password;

  @Override
  public String toString() {
    return "RegisterForm{" +
      "username='" + username + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
}
