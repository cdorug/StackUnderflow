package com.assignment.backend.service;

import com.assignment.backend.entity.Question;
import com.assignment.backend.entity.Tag;
import com.assignment.backend.entity.Vote;
import com.assignment.backend.repo.QuestionRepository;
import com.assignment.backend.repo.TagRepository;
import com.assignment.backend.repo.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    VoteRepository voteRepository;

    public List<Question> retrieveQuestions() {return (List<Question>) questionRepository.findAll();}

    public List<Question> retrieveQuestionsSortedDate() {
        List<Question> sortedQuestions = (List <Question>) questionRepository.findAll();
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
        return sortedQuestions;
    }

    public Optional<Question> retrieveQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public void deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
    }

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Tag saveTag(Tag tag) { return tagRepository.save(tag); }

    public Optional<Tag> retrieveTagByTagText(String tagText) { return tagRepository.findByTagText(tagText); }

    public Vote saveVote(Vote vote) { return voteRepository.save(vote); }

    public Question updateVotes(Long questionId, Long votes) {
        Optional<Question> question = questionRepository.findById(questionId);
        if(question.isPresent()) {
            Question updatedQuestion = question.get();
            updatedQuestion.setVotes(votes);
            return questionRepository.save(updatedQuestion);
        } else {
            System.out.println("Can't update votes, question doesn't exist!");
            return null;
        }
    }
}