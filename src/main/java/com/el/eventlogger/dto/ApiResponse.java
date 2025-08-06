package com.el.eventlogger.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
  private boolean success;
  private String message;
  private T data;

  public ApiResponse(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>(true, message, data);
  }

  public static <T> ApiResponse<T> failure(String message, T data) {
    return new ApiResponse<>(false, message, data);
  }
}
