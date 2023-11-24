package com.project.Restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {

  // 직렬화와 연관, 1은 int 데이터형 정수 리터럴, 1L은 long 데이터형 long 정수 리터럴
  private static final long serialVersionUID = 1L;
  public DataNotFoundException(String message) {
    super(message);
  }
}


