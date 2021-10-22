package com.akshay.app.controller;

import com.akshay.app.model.Comment;
import com.akshay.app.model.Post;
import com.akshay.app.service.CommentService;
import com.akshay.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class CommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/comment/{id}")
    public String showCommentForm(@PathVariable(value = "id") long id, Model model) {
        Comment comment = new Comment();
        model.addAttribute(comment);
        model.addAttribute("id", id);
        return "comment";
    }

    @PostMapping("/addComment/{id}")
    public String addComment(@PathVariable(value = "id") long id, @ModelAttribute Comment comment) {
        Post post = postService.findPostById(id);
        post.addComment(new Comment(comment));
        postService.save(post);
        return "redirect:/showPost/" + id;
    }

    @RequestMapping(path = "/deleteComment", method = RequestMethod.GET)
    public String deleteComment(@RequestParam(value = "comment_id", required = false) long commentId,
                                @RequestParam(value = "post_id", required = false) long id, Principal principal, Model model) {
        Post post = postService.findPostById(id);
        if (isPrincipalOwnerOfPost(principal, post)) {
            commentService.deleteCommentById(commentId);
            return "redirect:/showPost/" + id;
        } else {
            return "redirect:/showPost/" + id;
        }
    }

    @RequestMapping(path = "/updateComment", method = RequestMethod.GET)
    public String updateComment(@RequestParam(value = "comment_id", required = false) long commentId,
                                @RequestParam(value = "post_id", required = false) long postId, Model model, Principal principal) {
        Post post = postService.findPostById(postId);
        if (isPrincipalOwnerOfPost(principal, post)) {
            Comment comment = commentService.findCommentById(commentId);
            model.addAttribute(comment);
            model.addAttribute("id", postId);
            return "updateComment";
        } else {
            return "redirect:/showPost/" + postId;
        }
    }

    @PostMapping("/updateCommentDetails/{id}")
    public ModelAndView updateCommentDetails(@PathVariable(value = "id") long id, @ModelAttribute Comment comment) {
        commentService.updateCommentById(comment);
        return new ModelAndView("redirect:/showPost/{id}");
    }

    private boolean isPrincipalOwnerOfPost(Principal principal, Post post) {
        return principal != null && (principal.getName()).equals(post.getUser().getEmail());
    }
}
