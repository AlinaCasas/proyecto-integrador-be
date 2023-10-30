package com.proyecto.integrador.auth;

import com.proyecto.integrador.config.JwtService;
import com.proyecto.integrador.exceptions.BadRequestException;
import com.proyecto.integrador.token.Token;
import com.proyecto.integrador.token.TokenRepository;
import com.proyecto.integrador.token.TokenType;
import com.proyecto.integrador.user.Role;
import com.proyecto.integrador.user.User;
import com.proyecto.integrador.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  private User createUser (RegisterRequest request) {
    return User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
  }

  private AuthenticationResponse saveUser(User user) {
    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public AuthenticationResponse rootRegister(RegisterRequest request) {
    try {
      User user = this.createUser(request);
      user.setRole(Role.ADMIN);
      User savedUser = repository.save(user);
      String jwtToken = jwtService.generateToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(savedUser, jwtToken);
      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    } catch (Exception e) {
      throw new BadRequestException("The admin already exists");
    }
  }

  public AuthenticationResponse register(RegisterRequest request) {
    try {
      User user = this.createUser(request);
      User savedUser = repository.save(user);
      String jwtToken = jwtService.generateToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(savedUser, jwtToken);
      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    } catch (Exception e) {
    throw new BadRequestException("The email is taken, use another one");
    }
  }

  public AuthenticationResponse login(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new BadRequestException("User not found"));
    if (user.getDeletedAt() != null) throw new BadRequestException("The user is deleted, please contact support.");
    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      User user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
