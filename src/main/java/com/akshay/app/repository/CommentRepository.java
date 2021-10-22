package com.akshay.app.repository;

import com.akshay.app.model.Comment;
import com.akshay.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Transactional
    @Modifying
    @Query("update Comment p set p.name = :name , p.email = :email , p.message = :message WHERE p.id = :id")
    void updateCommentById(@Param("id") long id, @Param("name") String name, @Param("email") String email, @Param("message") String message);

    @Query("SELECT p FROM Comment p WHERE p.id = :id")
    public Comment findCommentById(long id);
}
