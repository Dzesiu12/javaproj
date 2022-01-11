package com.example.restauracja.filters;

import com.example.restauracja.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


public class JwtFilter extends BasicAuthenticationFilter {
  public JwtFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (!request.getRequestURI().startsWith("/api")) {
      chain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return;
    }

    try {
      UsernamePasswordAuthenticationToken auth = getAuthByAuthHeader(authHeader);
      System.out.println(auth);
      SecurityContextHolder.getContext().setAuthentication(auth);
      chain.doFilter(request, response);
    } catch (Exception e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
  }

  private UsernamePasswordAuthenticationToken getAuthByAuthHeader(String authHeader) {
    Jws<Claims> claims = JwtUtils.getClaimsFromHeader(authHeader);
    String username = claims.getBody().get("id").toString();
    String role = claims.getBody().get("role").toString();
    return new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(new SimpleGrantedAuthority(role)));
  }
}
