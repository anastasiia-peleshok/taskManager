package com.beech.taskmanager.controller;

import com.beech.taskmanager.entity.Role;
import com.beech.taskmanager.entity.User;
import com.beech.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/{userEmail}")
    public ResponseEntity<User> getUser(@PathVariable String userEmail) {
        System.out.printf("getUser: %s", userEmail);
        return ResponseEntity.ok(userService.findByEmail(userEmail));
    }

    @PostMapping("/admin/{userEmail}")
    public ResponseEntity<User> grantRole(@PathVariable String userEmail, @RequestParam Role role) {
        userService.changeRole(userEmail, role);
        return ResponseEntity.ok(userService.findByEmail(userEmail));
    }

    @PostMapping("{userEmail}/changePassword")
    public ResponseEntity changePassword(@PathVariable String userEmail, @RequestParam String password) {
        User user = userService.findByEmail(userEmail);
        userService.changePassword(user, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
