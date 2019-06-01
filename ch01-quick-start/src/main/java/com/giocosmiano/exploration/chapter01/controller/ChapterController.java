package com.giocosmiano.exploration.chapter01.controller;

import com.giocosmiano.exploration.chapter01.domain.Chapter;
import com.giocosmiano.exploration.chapter01.repository.ChapterRepository;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChapterController {
    private final ChapterRepository repository;

    public ChapterController(ChapterRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/chapters")
    public Flux<Chapter> listing() {
        return repository.findAll();
    }
}