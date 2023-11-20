package com.project.Restaurant.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        // 실패 이유에 따라 다른 메시지 설정
        if (exception instanceof BadCredentialsException) {
            errorMessage = "잘못된 사용자 이름 또는 비밀번호입니다.";
        } else if (exception instanceof DisabledException) {
            errorMessage = "계정이 비활성화되었습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof LockedException) {
            errorMessage = "계정이 잠겼습니다. 잠시 후 다시 시도하세요.";
        } else {
            errorMessage = "로그인에 실패했습니다.";
        }

        // 에러 메시지를 request 속성에 설정
        request.setAttribute("errorMessage", errorMessage);

        // 로그인 실패 후 로그인 페이지로 리다이렉션
        request.getRequestDispatcher("/member/login?error").forward(request, response);
    }
}
