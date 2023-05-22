package com.assignment.backend.repo;

import com.assignment.backend.entity.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
