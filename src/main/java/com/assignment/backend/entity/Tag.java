package com.assignment.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_text")
    private String tagText;
}
