package com.el.eventlogger.controller;

import com.el.eventlogger.domain.User;
import com.el.eventlogger.dto.LoginRequestDTO;
import com.el.eventlogger.dto.LoginResponseDTO;
import com.el.eventlogger.dto.SignupRequestDTO;
import com.el.eventlogger.exception.BadRequestException;
import com.el.eventlogger.repository.UserRepository;
import com.el.eventlogger.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
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
  public ResponseEntity<LoginResponseDTO> signup(@Valid @RequestBody SignupRequestDTO dto) {
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

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // Keep your existing method
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
    User user = userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new BadRequestException("Invalid email or password"));

    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new BadRequestException("Invalid email or password");
    }

    String token = jwtTokenProvider.generateToken(user.getEmail());
    LoginResponseDTO response = new LoginResponseDTO(token, user.getEmail());

    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Map<String, Object>> getToken(
          @RequestParam String username,
          @RequestParam String password,
          @RequestParam(defaultValue = "password") String grant_type) {

    User user = userRepository
            .findByEmail(username)
            .orElseThrow(() -> new BadRequestException("Invalid email or password"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadRequestException("Invalid email or password");
    }

    String token = jwtTokenProvider.generateToken(user.getEmail());

    Map<String, Object> response = new HashMap<>();
    response.put("access_token", token);
    response.put("token_type", "Bearer");
    response.put("expires_in", 3600);
    response.put("user_email", user.getEmail());

    return ResponseEntity.ok(response);
  }
}
