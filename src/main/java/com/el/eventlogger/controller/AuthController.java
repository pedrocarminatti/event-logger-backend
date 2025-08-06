package com.el.eventlogger.controller;

import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.*;
import com.el.eventlogger.exception.BadRequestException;
import com.el.eventlogger.repository.UserRepository;
import com.el.eventlogger.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthController(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<LoginResponseDTO>> signup(
      @Valid @RequestBody SignupRequestDTO dto) {
    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
      throw new BadRequestException("Email already registered");
    }

    User user =
        User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .build();

    userRepository.save(user);

    String token = jwtTokenProvider.generateToken(user.getEmail());
    LoginResponseDTO response = new LoginResponseDTO(token, user.getEmail());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Signup successful", response));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
      @Valid @RequestBody LoginRequestDTO dto) {
    User user =
        userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new BadRequestException("Invalid email or password"));

    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new BadRequestException("Invalid email or password");
    }

    String token = jwtTokenProvider.generateToken(user.getEmail());
    LoginResponseDTO response = new LoginResponseDTO(token, user.getEmail());

    return ResponseEntity.ok(ApiResponse.success("Login successful", response));
  }
}
