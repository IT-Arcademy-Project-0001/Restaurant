package com.project.Restaurant.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {
    @NotEmpty(message = "사용자ID는 필수입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수입니다.")
    private String passwordConfirm;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email
    private String email;
}
