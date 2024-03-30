package com.root.signaturehandler.controllers;

import com.root.signaturehandler.dtos.in.RegisterUserDTO;
import com.root.signaturehandler.dtos.out.UserCreatedDTO;
import com.root.signaturehandler.entities.User;
import com.root.signaturehandler.services.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.mapper.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreatedDTO> registerUser(@RequestBody @Valid RegisterUserDTO dto) {
        User createdUser = this.userService.registerUser(dto.toUser());

        UserCreatedDTO userCreatedDto = new UserCreatedDTO(createdUser);

        return ResponseEntity.status(201).body(userCreatedDto);
    }
}
