package com.example.restauracja.utils;

import com.example.restauracja.models.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtUtils {
  public static String getTokenFromHeader(String header) {
    return header.replace("Bearer ", "");
  }

  public static Jws<Claims> getClaimsFromHeader(String header) {
    return getClaimsFromToken(getTokenFromHeader(header));
  }

  public static Jws<Claims> getClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey("kiwi".getBytes()).parseClaimsJws(token);
  }

  public static String getUserIdFromHeader(String header) {
    return getClaimsFromHeader(header).getBody().get("id").toString();
  }

  public static String getUserIdFromToken(String token) {
    return getClaimsFromToken(token).getBody().get("id").toString();
  }

  public static String getTokenFromUser(AppUser user) {
    return Jwts.builder()
      .claim("id", user.getUsername())
      .claim("role", user.getAuthorities())
      .signWith(SignatureAlgorithm.HS256, "kiwi".getBytes())
      .compact();
  }
}
