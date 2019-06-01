package com.giocosmiano.exploration.springboot.repository;

import com.giocosmiano.exploration.springboot.domain.Chapter;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository
        extends ReactiveCrudRepository<Chapter, String> {
}