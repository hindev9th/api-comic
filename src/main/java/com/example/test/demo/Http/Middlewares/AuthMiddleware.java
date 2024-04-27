package com.example.test.demo.Http.Middlewares;

import com.example.test.demo.Enum.ApiStatus;
import com.example.test.demo.Helpers.HttpServletResponseHelper;
import com.example.test.demo.Http.Responses.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class AuthMiddleware extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponseHelper servletResponse = new HttpServletResponseHelper(response);
        List<String> urls = Arrays.asList(
                "/login",
                "/register",
                "/comics",
                "/chapters"
        );

        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
        if (urls.contains(request.getRequestURI())){
            filterChain.doFilter(wrapper,response);
            return;
        }
        servletResponse.setApiResponse(new ErrorResponse<>(ApiStatus.UNAUTHORIZED,"Unauthorized!", HttpStatus.UNAUTHORIZED)).toResponse();
        return;
    }
}
