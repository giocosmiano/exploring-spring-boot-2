package com.giocosmiano.exploration.chapter06.comments.repository;

import com.giocosmiano.exploration.chapter06.comments.domain.Comment;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentWriterRepository
        extends Repository<Comment, String> {

    /*
     By extending Spring Data Commons' Repository interface, it will be picked up as a repository. Being
     an empty interface, it comes with no predefined operations

     save() operation to store a new comment (and return it after it gets saved). If the ID
     value is null, Spring Data MongoDB will automatically generate a unique string value for us

     Spring Data requires a findOne() operation in order to perform saves because that's what it uses to
     fetch what we just saved in order to return it

     All of these method signatures use Reactor Mono types

     This repository is focused on writing data into MongoDB and nothing more. Even though it has a
     findOne(), it's not built for reading data
     */
    Flux<Comment> saveAll(Flux<Comment> newComment);

    Mono<Comment> save(Comment newComment);

    Mono<Comment> findById(String id);

    Mono<Void> deleteAll();
}
