package com.assignment.backend.controller;

import com.assignment.backend.entity.User;
import com.assignment.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.rmi.ServerException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stackoverflow/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/createUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User newUser) throws ServerException {
        User user = userService.saveUser(newUser);
        if(user == null) {
            throw new ServerException("create user failed");
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    @PutMapping("/editUser/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser, @PathVariable("id") Long id) throws ServerException {
        Optional<User> oldUser = userService.retrieveUserById(id);
        if(oldUser.isPresent()) {
            userService.saveUser(updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        else { throw new ServerException("edit user by id failed"); }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.retrieveUsers(), HttpStatus.OK);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userService.retrieveUserById(id);
        if(user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/deleteUserById/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
    }

}
