package com.project.Restaurant.Member;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordResetFrom {
  @NotBlank(message = "비밀번호는 필수입니다.")
  private String password;

  @NotBlank(message = "비밀번호 확인은 필수입니다.")
  private String passwordConfirm;
}