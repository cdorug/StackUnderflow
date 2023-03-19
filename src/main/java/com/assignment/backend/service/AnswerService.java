package com.assignment.backend.service;

import com.assignment.backend.entity.Answer;
import com.assignment.backend.repo.AnswerRepository;
import com.assignment.backend.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;

    public List<Answer> retrieveAnswers() {
        return (List<Answer>) answerRepository.findAll();
    }

    public Optional<Answer> retrieveAnswerById(Long id) {
        return answerRepository.findById(id);
    }

    public void deleteAnswerById(Long id) {
        answerRepository.deleteById(id);
    }

    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> retrieveAnswerOfQuestion(Long searchedQuestionId) {
        List<Answer> answersOfQuestion = (List<Answer>) answerRepository.findAll();
        answersOfQuestion = answersOfQuestion.stream().filter(answer -> answer.getQuestionId()
                            .equals(searchedQuestionId)).collect(Collectors.toList());
        return answersOfQuestion;
    }
}