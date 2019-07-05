package com.giocosmiano.exploration.chapter08.comments.domain;

import lombok.Data;

@Data
public class Comment {

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