package com.akshay.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
//
//        PostRepository postRepository = context.getBean(PostRepository.class);
//
//        Post post = new Post("title","excerpt","Akshaytomar","akshkk");
//
//        Post post1 = postRepository.save(post);
//
//        System.out.println(post1);
    }
}