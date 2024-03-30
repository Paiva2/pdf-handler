package com.root.signaturehandler.presentation.controllers;

import com.root.signaturehandler.presentation.dtos.in.AuthUserDTO;
import com.root.signaturehandler.presentation.dtos.in.RegisterUserDTO;
import com.root.signaturehandler.presentation.dtos.out.FetchUserProfileDTO;
import com.root.signaturehandler.presentation.dtos.out.UserAuthenticatedDTO;
import com.root.signaturehandler.presentation.dtos.out.UserCreatedDTO;
import com.root.signaturehandler.domain.entities.User;
import com.root.signaturehandler.domain.services.UserService;
import com.root.signaturehandler.presentation.utils.JwtAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtAdapter jwtAdapter;

    public UserController(UserService userService, JwtAdapter jwtAdapter) {
        this.userService = userService;
        this.jwtAdapter = jwtAdapter;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreatedDTO> registerUser(@RequestBody @Valid RegisterUserDTO dto) {
        User createdUser = this.userService.registerUser(dto.toUser());

        UserCreatedDTO userCreatedDto = new UserCreatedDTO(createdUser);

        return ResponseEntity.status(201).body(userCreatedDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticatedDTO> authenticateUser(@RequestBody @Valid AuthUserDTO dto) {

        User authenticated = this.userService.authUser(dto.toUser());

        String token = this.jwtAdapter.generate(authenticated.getId());

        UserAuthenticatedDTO userAuthenticatedDTO = new UserAuthenticatedDTO(token);

        return ResponseEntity.status(200).body(userAuthenticatedDTO);
    }

    @GetMapping("/profile")
    public ResponseEntity<FetchUserProfileDTO> fetchProfile(
            @RequestHeader(name = "Authorization") String authorizationHeader
    ) {
        String parseToken = this.jwtAdapter.verify(authorizationHeader.replace("Bearer ", ""));

        User user = this.userService.getProfile(UUID.fromString(parseToken));

        FetchUserProfileDTO fetchProfileDto = new FetchUserProfileDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt()
        );

        return ResponseEntity.status(200).body(fetchProfileDto);
    }
}
