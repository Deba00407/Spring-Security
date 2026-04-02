package com.debanjan.spring_security.controllers;

import com.debanjan.spring_security.dtos.UserResponseDTO;
import com.debanjan.spring_security.services.AdminService;
import com.debanjan.spring_security.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users/all")
    public ResponseEntity<SuccessResponse<List<UserResponseDTO>>> getAllUsers(){
        return ResponseEntity.ok(SuccessResponse.<List<UserResponseDTO>>builder()
                .data(adminService.getAllUsers())
                .status(HttpStatus.OK.value())
                .message("All users fetched successfully")
                .build());
    }
}
