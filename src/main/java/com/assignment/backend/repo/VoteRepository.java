package com.assignment.backend.repo;

import com.assignment.backend.entity.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {
}
