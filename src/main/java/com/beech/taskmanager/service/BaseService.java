package com.beech.taskmanager.service;

import com.beech.taskmanager.entity.User;
import com.beech.taskmanager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class BaseService {

    protected final UserRepository userRepository;

    public BaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkUserPermissions(User targetUser) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (currentUser == null || ((currentUser.getId().equals(targetUser.getId()) && currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))));

    }

    public boolean isAdmin(User targetUser) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
