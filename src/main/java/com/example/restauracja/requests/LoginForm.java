package com.example.restauracja.requests;

public class LoginForm {
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
