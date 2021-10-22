package com.akshay.app.service;

import com.akshay.app.model.Comment;
import com.akshay.app.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void deleteCommentById(long id){
        commentRepository.deleteById(id);
    }

    public void updateCommentById(Comment comment){
        commentRepository.updateCommentById(comment.getId(),comment.getName(),comment.getEmail(),comment.getMessage());
    }

    public Comment findCommentById(long id){
        return commentRepository.findCommentById(id);
    }
}
