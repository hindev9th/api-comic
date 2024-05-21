package com.example.test.demo.Http.Middlewares;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test.demo.Enum.ApiStatus;
import com.example.test.demo.Helpers.CommonHelper;
import com.example.test.demo.Helpers.HttpServletResponseHelper;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Models.User;
import com.example.test.demo.Reponsitories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.*;

@Component
@Order(1)
public class AuthMiddleware extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletResponseHelper servletResponse = new HttpServletResponseHelper(response);
        List<String> urls = Arrays.asList(
                "/login",
                "/register",
                "/comics",
                "/chapters",
                "/pages",
                "/configs/crawl-url"
        );

        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
        if (urls.contains(request.getRequestURI())){
            filterChain.doFilter(wrapper,response);
            return;
        }

        try {
            Map<String, Object> headers = getHeaders(wrapper);
            String token = headers.get("authorization").toString().replace("Bearer ","").replace("bearer ","");
            Algorithm algorithm = Algorithm.HMAC256(CommonHelper.getEnv("JWT_SECRET"));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            byte[] decodedBytes = Base64.getDecoder().decode(decodedJWT.getPayload());
            String decodedString = new String(decodedBytes);
            Map<String, Object> payload = CommonHelper.stringToJsonMap(decodedString);

            String userOid = payload.get("sub").toString();

            Optional<User> user = this.userRepository.findById(userOid);

            wrapper.setAttribute("auth", user.get());
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.get(), null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(wrapper,response);
        }catch (Exception e){
            servletResponse.setApiResponse(new ErrorResponse<>(ApiStatus.UNAUTHORIZED,"Unauthorized!", HttpStatus.UNAUTHORIZED)).toResponse();
            return;
        }
    }

    private Map<String, Object> getHeaders(ContentCachingRequestWrapper request) {
        Map<String, Object> header = new HashMap<>();

        Collections.list(request.getHeaderNames())
                .forEach(headerName -> Collections.list(request.getHeaders(headerName))
                        .forEach(headerValue -> header.put(headerName, headerValue)));

        return header;
    }
}
