package com.example.taskplanner.service;

import com.example.taskplanner.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TagService {

    List<Tag> findAll();
    Tag save(Tag tag);
    void deleteById(Long id);
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
    List<Tag> findAllById(List<Long> tagIds);
}
