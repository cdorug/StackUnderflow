package com.assignment.backend.repo;

import com.assignment.backend.entity.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Long> {
    Optional<Tag> findByTagText(String tagText);
}
