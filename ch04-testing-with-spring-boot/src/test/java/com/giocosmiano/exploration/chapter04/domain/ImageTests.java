package com.giocosmiano.exploration.chapter04.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTests {

    /*
     https://www.baeldung.com/lombok-ide
     https://stackoverflow.com/questions/41161076/adding-lombok-plugin-to-intellij-project
     To make Annotation work, such as Lombok, in IntelliJ
     1) Install Lombok plugin
     2) Go to Settings, Build/Execution/Deployment, Compiler, Enable `Annotation Processors`
     */
    @Test
    public void imagesManagedByLombokShouldWork() {
        Image image = new Image("id", "file-name.jpg");
        assertThat(image.getId()).isEqualTo("id");
        assertThat(image.getName()).isEqualTo("file-name.jpg");
    }
}