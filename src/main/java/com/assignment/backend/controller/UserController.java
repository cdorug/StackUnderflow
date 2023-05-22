package com.assignment.backend.controller;

import com.assignment.backend.DTO.UserDTO;
import com.assignment.backend.authentication.JwtService;
import com.assignment.backend.entity.User;
import com.assignment.backend.service.EmailService;
import com.assignment.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.rmi.ServerException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stackoverflow/users")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User validatedUser = userService.validateUser(username, password);

        if (validatedUser != null) {
            if(validatedUser.getRole().equals("banned")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User banned.");
            }
            else {
                // Generate JWT token
                String token = jwtService.generateToken(validatedUser.getUsername(), validatedUser.getPassword());

                return ResponseEntity.ok(token);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @PostMapping(value = "/createUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User newUser) throws ServerException {
        List<User> users = userService.retrieveUsers();
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getUsername().equals(newUser.getUsername()))
                .collect(Collectors.toList());
        List<User> furtherFilteredUsers = filteredUsers.stream()
                .filter(user -> user.getEmail().equals(newUser.getEmail()))
                .collect(Collectors.toList());
        if(furtherFilteredUsers.size() == 0) {
            // no user with same username or email
            User user = userService.saveUser(newUser);
            if(user == null) {
                return null;
            } else {
                //
                String to = user.getEmail();
                String from = "stackunderflow.com";
                String subject = "Welcome to Stackunderflow!";
                String body = "\n\nHello " + user.getUsername() + ", welcome to the platform!";
                emailService.sendSimpleMessage(to, from, subject, body);
                //
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
        }
        else {
            return null;
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

    @PutMapping("/banUser")
    public ResponseEntity<User> banUser(@RequestBody User bannedUser) throws ServerException {
        Optional<User> oldUser = userService.retrieveUserById(bannedUser.getUserId());
        bannedUser.setRole("banned");
        if(oldUser.isPresent()) {
            userService.saveUser(bannedUser);
            //
            String to = bannedUser.getEmail();
            String from = "stackunderflow.com";
            String subject = "Ban notification";
            String body = bannedUser.getUsername() + ",\n\nYou have been hit by the BAN hammer. Have a nice day.";
            emailService.sendSimpleMessage(to, from, subject, body);
            //
            return new ResponseEntity<>(bannedUser, HttpStatus.OK);
        }
        else { throw new ServerException("ban user failed"); }
    }

    @PutMapping("/unbanUser")
    public ResponseEntity<User> unbanUser(@RequestBody User unbannedUser) throws ServerException {
        Optional<User> oldUser = userService.retrieveUserById(unbannedUser.getUserId());
        unbannedUser.setRole("user");
        if(oldUser.isPresent()) {
            userService.saveUser(unbannedUser);
            return new ResponseEntity<>(unbannedUser, HttpStatus.OK);
        }
        else { throw new ServerException("ban user failed"); }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String token) {
        boolean status = jwtService.validateToken(token);
        System.out.println("Got token: " + token);
        System.out.println("Authenticated status: " + status);
        if(status) {
            return new ResponseEntity<>(userService.retrieveUsers(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
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

    @GetMapping("/getUserByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username) {
        Optional<User> user = userService.retrieveUserByUsername(username);
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
