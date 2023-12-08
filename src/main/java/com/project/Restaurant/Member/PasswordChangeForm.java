package com.project.Restaurant.Member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeForm {
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String passwordConfirm;

    @NotBlank(message = "현재비밀번호는 필수입니다.")
    private String oldPassword;
}
