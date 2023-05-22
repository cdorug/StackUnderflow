package com.assignment.backend.DTO;

import com.assignment.backend.entity.Answer;
import com.assignment.backend.entity.Question;
import com.assignment.backend.entity.User;
import java.util.Set;

public class UserDTO {

    private Long userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private String profilePicture;
    private float score;
    private String location;
    private Set<Question> questions;
    private Set<Answer> answers;

    public UserDTO() {
    }

    public UserDTO(Long userId, String username, String lastName, String firstName, String email, String profilePicture,
                   Long score, String location, Set<Question> questions, Set<Answer> answers) {
        this.userId = userId;
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.score = score;
        this.location = location;
        this.questions = questions;
        this.answers = answers;
    }

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.profilePicture = user.getProfilePicture();
        this.score = user.getScore();
        this.location = user.getLocation();
        this.questions = user.getQuestions();
        this.answers = user.getAnswers();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public float getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
