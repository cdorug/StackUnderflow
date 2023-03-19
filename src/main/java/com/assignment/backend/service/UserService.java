package com.assignment.backend.service;

import com.assignment.backend.entity.User;
import com.assignment.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> retrieveUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> retrieveUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        /*
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
         */
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
