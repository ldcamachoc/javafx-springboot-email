package com.lakesidess;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class SpringBootJavaFXApplication {

    public static void main(String[] args) {
        // This is how normal Spring Boot app would be launched
        //SpringApplication.run(SpringBootExampleApplication.class, args);

        Application.launch(JavaFxApplication.class, args);
    }
}
