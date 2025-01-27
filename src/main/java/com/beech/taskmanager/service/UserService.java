package com.beech.taskmanager.service;

import com.beech.taskmanager.entity.Role;
import com.beech.taskmanager.entity.User;


public interface UserService {
    User findByEmail(String email);

    void changeRole(String email, Role role);

    void changePassword(User user, String password);
}
