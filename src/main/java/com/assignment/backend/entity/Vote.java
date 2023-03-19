package com.assignment.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @Column(name = "vote_id")
    private Long voteId;

    @Column(name = "vote")
    private boolean vote;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "user_id")
    private Long userId;

}
