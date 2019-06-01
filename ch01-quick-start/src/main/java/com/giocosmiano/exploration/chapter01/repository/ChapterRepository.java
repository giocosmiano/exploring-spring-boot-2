package com.giocosmiano.exploration.chapter01.repository;

import com.giocosmiano.exploration.chapter01.domain.Chapter;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository
        extends ReactiveCrudRepository<Chapter, String> {
}