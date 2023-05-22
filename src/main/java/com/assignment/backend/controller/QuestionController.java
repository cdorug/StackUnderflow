package com.assignment.backend.controller;

import com.assignment.backend.authentication.JwtService;
import com.assignment.backend.entity.*;
import com.assignment.backend.service.AnswerService;
import com.assignment.backend.service.QuestionService;
import com.assignment.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/stackoverflow/questions")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    AnswerService answerService;

    @Autowired
    JwtService jwtService;

    @PostMapping(value = "/createQuestion", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Question> createQuestion(@RequestBody Question newQuestion) throws ServerException {
        Set<Tag> tagsFromRequest = newQuestion.getTags();
        Set<Tag> newTags = new HashSet<>();
        for(Tag tag: tagsFromRequest) {
            Optional<Tag> foundTag = questionService.retrieveTagByTagText(tag.getTagText());
            if(foundTag.isPresent()) {
                newTags.add(foundTag.get());
            } else {
                Tag newTag = questionService.saveTag(tag);
                newTags.add(newTag);
            }
        }
        newQuestion.setTags(newTags);

        Question question = questionService.saveQuestion(newQuestion);
        if(question == null) {
            throw new ServerException("create question failed");
        } else {
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }
    }

    @PostMapping(value = "/createVote", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Vote> createVote(@RequestBody Vote newVote) throws ServerException {
        try {
            boolean verdict = newVote.getVote();

            if(newVote.getQuestion() != null) {
                // it's a question vote
                Long questionVotes = newVote.getQuestion().getVotes();
                float userScore = newVote.getQuestion().getAuthor().getScore();

                if(verdict) {
                    questionVotes ++;
                    userScore += 2.5;
                } else {
                    questionVotes --;
                    userScore -= 1.5;
                }
                questionService.updateVotes(newVote.getQuestion().getQuestionId(), questionVotes);
                userService.updateScore(newVote.getQuestion().getAuthor().getUserId(), userScore);
            } else {
                // it's an answer vote
                Long answerVotes = newVote.getAnswer().getVotes();
                float answerAuthorScore = newVote.getAnswer().getAuthor().getScore();

                if(verdict) {
                    answerVotes ++;
                    answerAuthorScore += 5;
                }
                else {
                    float voterScore = newVote.getUser().getScore();
                    // voter also loses points;
                    answerVotes --;
                    answerAuthorScore -= 2.5;
                    voterScore -= 1.5;
                    userService.updateScore(newVote.getUser().getUserId(), voterScore);
                }

                answerService.updateVotes(newVote.getAnswer().getAnswerId(), answerVotes);
                userService.updateScore(newVote.getAnswer().getAuthor().getUserId(), answerAuthorScore);

            }
            Vote vote = questionService.saveVote(newVote); // save vote
            return new ResponseEntity<>(vote, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/editQuestion/{id}")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question updatedQuestion, @PathVariable("id") Long id) throws ServerException {
        Optional<Question> oldQuestion = questionService.retrieveQuestionById(id);
        if(oldQuestion.isPresent()) {
            updatedQuestion.setAnswers(oldQuestion.get().getAnswers());
            updatedQuestion.setVoteReferences(oldQuestion.get().getVoteReferences());
            questionService.saveQuestion(updatedQuestion);
            return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
        }
        else { throw new ServerException("edit question by id failed"); }
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions(@RequestHeader("Authorization") String token) {
        boolean status = jwtService.validateToken(token);
        System.out.println("Got token: " + token);
        System.out.println("Authenticated status: " + status);
        if(status) {
            return new ResponseEntity<>(questionService.retrieveQuestions(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

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
        List<Question> sortedQuestions = questionService.retrieveQuestionsSortedDate();
        return new ResponseEntity<>(sortedQuestions, HttpStatus.OK);
    }

    @DeleteMapping("/deleteQuestionById/{id}")
    public void deleteQuestionById(@PathVariable("id") Long id) {
        System.out.println("Was asked to delete question with id: " + id);
        questionService.deleteQuestionById(id);
    }

}