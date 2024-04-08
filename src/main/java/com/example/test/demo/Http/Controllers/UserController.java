package com.example.test.demo.Http.Controllers;

import com.example.test.demo.Models.User;
import com.example.test.demo.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<?> getUsers(){
        return this.userService.getList();
    }

    @GetMapping(value = "users/search")
    public ResponseEntity<?> getUserByUsername(@RequestParam(name = "u")Optional<String> u){
        return this.userService.getUserByUsername(u);
    }

}
