package com.giocosmiano.exploration.chapter06.comments.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Comment {

    @Id
    private String id;
    private String imageId;
    private String comment;

    public Comment() {}

    public Comment(String id, String imageId, String comment) {
        this.id = id;
        this.imageId = imageId;
        this.comment = comment;
    }
}