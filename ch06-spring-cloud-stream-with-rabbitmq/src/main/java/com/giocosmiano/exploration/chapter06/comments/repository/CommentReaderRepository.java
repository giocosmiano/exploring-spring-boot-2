package com.giocosmiano.exploration.chapter06.comments.repository;

import com.giocosmiano.exploration.chapter06.comments.domain.Comment;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;

public interface CommentReaderRepository
        extends Repository<Comment, String> {
    Flux<Comment> findByImageId(String imageId);
}
