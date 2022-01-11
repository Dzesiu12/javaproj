package com.example.restauracja.models;

import com.example.restauracja.enums.ResponseType;

public class Response {
  public ResponseType responseType;
  public String message;
  public Object data;

  public Response(ResponseType responseType, String message) {
    this.responseType = responseType;
    this.message = message;
  }

  public Response(ResponseType responseType, String message, Object data) {
    this.responseType = responseType;
    this.message = message;
    this.data = data;
  }
}
