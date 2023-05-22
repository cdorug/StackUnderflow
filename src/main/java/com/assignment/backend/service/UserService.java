package com.assignment.backend.service;

import com.assignment.backend.entity.Question;
import com.assignment.backend.entity.User;
import com.assignment.backend.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
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

    public Optional<User> retrieveUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public User validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            System.out.println("Validated User: " + user.get().getUsername());
            if(user.get().getPassword().equals(password)) {
                return user.get();
            }
            else {
                return null;
            }
        } else {
            return null;
        }
    }

    public User updateScore(Long userId, float score) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setScore(score);
            return userRepository.save(updatedUser);
        } else {
            System.out.println("Can't update score, user doesn't exist!");
            return null;
        }
    }

}
