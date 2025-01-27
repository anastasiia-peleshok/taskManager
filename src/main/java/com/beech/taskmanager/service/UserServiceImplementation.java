package com.beech.taskmanager.service;

import com.beech.taskmanager.entity.Role;
import com.beech.taskmanager.entity.User;
import com.beech.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation extends BaseService implements UserService {

    private final PasswordEncoder passwordEncoder;


    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get();
    }

    @Override
    public void changeRole(String email, Role role) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!isAdmin(user.get())) {
            throw new RuntimeException("User is not admin");
        }
        if (user.get().getRole().equals(role)) {
            throw new RuntimeException("User already has role " + role);
        }
        user.get().setRole(role);
        userRepository.save(user.get());
    }

    @Override
    public void changePassword(User user, String password) {
        if (!checkUserPermissions(user)) {
            throw new RuntimeException("You do not have permissions to manage this user ");
        }
        if (user.getPassword().equals(passwordEncoder.encode(password))) {
            throw new RuntimeException("You can not change the password for the same one");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

    }
}
