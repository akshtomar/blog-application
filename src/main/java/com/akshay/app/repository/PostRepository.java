package com.akshay.app.repository;

import com.akshay.app.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Transactional
    @Modifying
    @Query("update Post p set p.author = :author , p.title = :title , p.content = :content, p.excerpt = :excerpt   WHERE p.id = :id")
    void updatePostById(@Param("id") Long id, @Param("author") String author, @Param("title") String title, @Param("content") String content,
                        @Param("excerpt") String excerpt);

    @Query("SELECT p FROM Post p WHERE CONCAT(p.author, p.title, p.excerpt, p.content) LIKE %?1%")
    public Page<Post> search(String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id = :id")
    public Post findPostById(long id);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByOrderByPublishedAtDesc(Pageable pageable);

    Page<Post> findAllByOrderByPublishedAt(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author LIKE %?1%")
    Page<Post> findByAuthor(String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %?1%")
    Page<Post> findByTitle(String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.excerpt LIKE %?1%")
    Page<Post> findByExcerpt(String keyword, Pageable pageable);

    @Query("select distinct author from Post")
    List<String> findDistinctAuthor();

    @Query("SELECT p FROM Post p WHERE p.author LIKE %?1%")
    List<Post> findByAuthor(String author);

}
