package com.project.Restaurant;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestFilter  implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUri = httpServletRequest.getRequestURI();

        HttpSession session = httpServletRequest.getSession();
        if(requestUri.equals("/owner/login") || requestUri.equals("/customer/login")) {
            session.setAttribute("requestUri", requestUri);
        }
        chain.doFilter(request, response);
    }
}
