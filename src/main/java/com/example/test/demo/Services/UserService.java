package com.example.test.demo.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.test.demo.Enum.ApiStatus;
import com.example.test.demo.Helpers.CommonHelper;
import com.example.test.demo.Http.Responses.ApiResponse;
import com.example.test.demo.Http.Responses.ErrorResponse;
import com.example.test.demo.Http.Responses.SuccessResponse;
import com.example.test.demo.Models.User;
import com.example.test.demo.Reponsitories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    public final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> getList() {
        List<User> users = this.userRepository.findAll();
        return new SuccessResponse<>(users).responseEntity();
    }

    public ResponseEntity<?> getUserByUsername(Optional<String> username) {
        Optional<User> user = this.userRepository.findUserByUsername(username.get());
        return new SuccessResponse<>(user).responseEntity();
    }

    public ResponseEntity<?> register(User user) {
        Optional<User> valid = this.userRepository.findUserByUsername(user.getUsername());
        if (valid.isPresent()) {
            return new ErrorResponse<>(ApiStatus.FOUND, "Your username already exist!", HttpStatus.FOUND).responseEntity();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return new SuccessResponse<>(user).responseEntity();
    }

    public ResponseEntity<?> login(User user) {
        Optional<User> valid = this.userRepository.findUserByUsername(user.getUsername());
        if (valid.isEmpty()){
            return new ErrorResponse<>(ApiStatus.NOT_FOUND, "Your username or password incorrect", HttpStatus.NOT_FOUND).responseEntity();
        }
        if (!passwordEncoder.matches(user.getPassword(), valid.get().getPassword())) {
            return new ErrorResponse<>(ApiStatus.NOT_FOUND, "Your username or password incorrect", HttpStatus.NOT_FOUND).responseEntity();
        }
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        long timeNow = new Date().getTime();
        long timeEx = timeNow + 86400000L * 24 * 30;
        Algorithm algorithm = Algorithm.HMAC256(CommonHelper.getEnv("JWT_SECRET"));
        String token = JWT.create().withIssuer(CommonHelper.getEnv("BASE_URL")).withExpiresAt(new Date(timeEx)).withSubject(valid.get().get_id()).withPayload(payload).sign(algorithm);

        result.put("username",valid.get().getUsername());
        result.put("name",valid.get().getName());
        result.put("avatar",valid.get().getAvatar());
        result.put("token",token);

        return new SuccessResponse<>(result).responseEntity();
    }
}
