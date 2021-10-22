package com.akshay.app.service;

import com.akshay.app.model.Post;
import com.akshay.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void save(Post post) {
        postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void updatePostById(Post post) {
        postRepository.updatePostById(post.getId(), post.getAuthor(), post.getTitle(), post.getContent(), post.getExcerpt());
    }

    public void deletePostById(long id) {
        this.postRepository.deleteById(id);
    }

    public Page<Post> listAllPost(String keyword, Pageable pageable) {
        return postRepository.search(keyword, pageable);
    }

    public Page<Post> getPage(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post findPostById(long id) {
        return postRepository.findPostById(id);
    }

    public Page<Post> sortByPublishedAtDesc(Pageable pageable) {
        return postRepository.findAllByOrderByPublishedAtDesc(pageable);
    }

    public Page<Post> sortByPublishedAt(Pageable pageable) {
        return postRepository.findAllByOrderByPublishedAt(pageable);
    }

    public Page<Post> findByAuthor(String keyword, Pageable pageable) {
        return postRepository.findByAuthor(keyword, pageable);
    }

    public Page<Post> findByTitle(String keyword, Pageable pageable) {
        return postRepository.findByTitle(keyword, pageable);
    }

    public Page<Post> findByExcerpt(String keyword, Pageable pageable) {
        return postRepository.findByExcerpt(keyword, pageable);
    }

    public List<String> findDistinctAuthor() {
        return postRepository.findDistinctAuthor();
    }

    public List<Post> findByAuthor(String author) {
        return postRepository.findByAuthor(author);
    }
}

