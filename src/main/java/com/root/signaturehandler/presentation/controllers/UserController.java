package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.presentation.dtos.in.RegisterUserDTO;
import com.root.signaturehandler.presentation.dtos.out.UserCreatedDTO;
import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
