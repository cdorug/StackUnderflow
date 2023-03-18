package com.assignment.backend.controller;

import com.assignment.backend.entity.Question;
import com.assignment.backend.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stackoverflow/questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping(value = "/createQuestion", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Question> createUser(@RequestBody Question newQuestion) throws ServerException {
        Question question = questionService.saveQuestion(newQuestion);
        if(question == null) {
            throw new ServerException("create question failed");
        } else {
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }
    }

    @PutMapping("/editQuestion/{id}")
    public ResponseEntity<Question> updateUser(@RequestBody Question updatedQuestion, @PathVariable("id") Long id) throws ServerException {
        Optional<Question> oldQuestion = questionService.retrieveQuestionById(id);
        if(oldQuestion.isPresent()) {
            questionService.saveQuestion(updatedQuestion);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        }
        else { throw new ServerException("edit question by id failed"); }
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllUsers() {
        return new ResponseEntity<>(questionService.retrieveQuestions(), HttpStatus.OK);
    }

    @GetMapping("/getQuestionById/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Long id) {
        Optional<Question> question = questionService.retrieveQuestionById(id);
        if(question.isPresent()) {
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/deleteQuestionById/{id}")
    public void deleteQuestionById(@PathVariable("id") Long id) {
        questionService.deleteQuestionById(id);
    }

}