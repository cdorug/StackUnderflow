package com.assignment.backend.controller;

import com.assignment.backend.entity.Answer;
import com.assignment.backend.service.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stackoverflow/answers")
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @PostMapping(value = "/createAnswer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer newAnswer) throws ServerException {
        Answer answer = answerService.saveAnswer(newAnswer);
        if(answer == null) {
            throw new ServerException("create answer failed");
        } else {
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }
    }

    @PutMapping("/editAnswer/{id}")
    public ResponseEntity<Answer> updateAnswer(@RequestBody Answer updatedAnswer, @PathVariable("id") Long id) throws ServerException {
        Optional<Answer> oldAnswer = answerService.retrieveAnswerById(id);
        if(oldAnswer.isPresent()) {
            answerService.saveAnswer(updatedAnswer);
            return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
        }
        else { throw new ServerException("edit answer by id failed"); }
    }

    @GetMapping("/getAllAnswers")
    public ResponseEntity<List<Answer>> getAllAnswers() {
        return new ResponseEntity<>(answerService.retrieveAnswers(), HttpStatus.OK);
    }

    @GetMapping("/getAnswerById/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable("id") Long id) {
        Optional<Answer> answer = answerService.retrieveAnswerById(id);
        if(answer.isPresent()) {
            return new ResponseEntity<>(answer.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @DeleteMapping("/deleteAnswerById/{id}")
    public void deleteAnswerById(@PathVariable("id") Long id) {
        answerService.deleteAnswerById(id);
    }

}