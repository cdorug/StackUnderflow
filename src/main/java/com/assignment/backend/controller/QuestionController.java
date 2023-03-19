package com.assignment.backend.controller;

import com.assignment.backend.entity.Question;
import com.assignment.backend.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stackoverflow/questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping(value = "/createQuestion", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Question> createQuestion(@RequestBody Question newQuestion) throws ServerException {
        Question question = questionService.saveQuestion(newQuestion);
        if(question == null) {
            throw new ServerException("create question failed");
        } else {
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }
    }

    @PutMapping("/editQuestion/{id}")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question updatedQuestion, @PathVariable("id") Long id) throws ServerException {
        Optional<Question> oldQuestion = questionService.retrieveQuestionById(id);
        if(oldQuestion.isPresent()) {
            questionService.saveQuestion(updatedQuestion);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        }
        else { throw new ServerException("edit question by id failed"); }
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
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

    @GetMapping("/getAllQuestionsSortedDate")
    public ResponseEntity<List<Question>> getQuestionsSortedDate() {
        List<Question> sortedQuestions = questionService.retrieveQuestions();
        Collections.sort(sortedQuestions, new Comparator() {
            public int compare(Object o1, Object o2) {
                Date d1 = ((Question) o1).getDate();
                Date d2 = ((Question) o2).getDate();
                int comp = d2.compareTo(d1);
                if(comp != 0) {
                    return comp;
                }
                Time t1 = ((Question) o1).getTime();
                Time t2 = ((Question) o2).getTime();
                return t2.compareTo(t1);
            }
        });
        return new ResponseEntity<>(sortedQuestions, HttpStatus.OK);
    }

    @DeleteMapping("/deleteQuestionById/{id}")
    public void deleteQuestionById(@PathVariable("id") Long id) {

        questionService.deleteQuestionById(id);
    }

}