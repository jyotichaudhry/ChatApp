package com.sample.chat.controller;

import com.sample.chat.dto.UserDto;
import com.sample.chat.entity.User;
import com.sample.chat.model.LoginRequest;
import com.sample.chat.model.TokenResponse;
import com.sample.chat.model.UserRequest;
import com.sample.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<TokenResponse> createUser(@RequestBody UserRequest request) {
        service.createUser(request);
        return authUser(LoginRequest.builder().email(request.getEmail()).password(request.getPassword()).build());
    }

    @GetMapping
    public ResponseEntity<User> getUserInfo(Principal principal) {
        return ResponseEntity.ok(service.getUserByEmail(principal.getName()));
    }

    @PutMapping("/image")
    public ResponseEntity<UserDto> setPictureToUser(@RequestPart("file") MultipartFile picture, Principal principal) throws IOException {
        return ResponseEntity.ok(service.setPicture(service.getUserByEmail(principal.getName()), picture));
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<UserDto>> getContacts(Principal principal) {
        return ResponseEntity.ok(service.getContacts(service.getUserByEmail(principal.getName())));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authUser(@RequestBody LoginRequest request) {
        this.authenticate(request);
        return ResponseEntity.ok(TokenResponse.builder().token(this.service.getToken(request)).build());
    }

    private void authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}
