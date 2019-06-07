package com.giocosmiano.exploration.chapter04.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTests {

    /*
     To make Lombok work
     1) Install Lombok plugin for IntelliJ
     2) Enable `Annotation Processors`
     */
    @Test
    public void imagesManagedByLombokShouldWork() {
        Image image = new Image("id", "file-name.jpg");
        assertThat(image.getId()).isEqualTo("id");
        assertThat(image.getName()).isEqualTo("file-name.jpg");
    }
}