package com.akshay.app.controller;

import com.akshay.app.model.Post;
import com.akshay.app.model.Role;
import com.akshay.app.model.Tag;
import com.akshay.app.model.User;
import com.akshay.app.service.PostService;
import com.akshay.app.service.TagService;
import com.akshay.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping("/addPost")
    public String addPost(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        String userRole = "";
        List<Role> roleList = user.getRoles();
        for (Role role : roleList) {
            String str = role.getName();
            if (str.equals("admin")) {
                userRole = "admin";
                break;
            }
        }
        Post post = new Post();
        if (!(userRole.equals("admin"))) {
            model.addAttribute(post);
            return "addPost";
        } else {
            model.addAttribute(post);
            return "admin_addPost";
        }
    }

    @PostMapping("/newPost")
    public String createNewPost(@ModelAttribute Post post, HttpServletRequest request, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        String userRole = "";
        List<Role> roleList = user.getRoles();
        for (Role role : roleList) {
            String str = role.getName();
            if (str.equals("admin")) {
                userRole = "admin";
                break;
            }
        }
        if (!(userRole.equals("admin"))) {
            post.setUser(user);
            post.setAuthor(user.getName());
        } else {
            post.setUser(user);
        }
        String tags = request.getParameter("allTags");
        String tagsWithComma = tags + ",";
        String[] data = tagsWithComma.split(",");

        for (String name : data) {
            Tag tag = tagService.findByName(name);
            if (tag != null) {
                post.getTags().add(tag);
                tag.getPosts().add(post);
            } else {
                Tag tagOne = tagService.save(new Tag(name));
                post.getTags().add(tagOne);
                tagOne.getPosts().add(post);
            }
        }
        postService.save(post);
        return "redirect:/home";
    }

    @GetMapping("/updatePost/{id}")
    public String updatePost(@PathVariable(value = "id") long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Post post = postService.findPostById(id);
        List<Role> roleList = user.getRoles();
        String userRole = "";
        for (Role role : roleList) {
            if ((role.getName()).equals("admin"))
                userRole = "admin";
        }
        if (userRole.equals("admin")) {
            model.addAttribute("post", post);
            return "updatePost";
        } else {
            if (isPrincipalOwnerOfPost(principal, post)) {
                model.addAttribute("post", post);
                return "updatePost";
            } else
                return "redirect:/home?error";
        }
    }

    @GetMapping("/deletePost/{id}")
    public String deletePost(@PathVariable(value = "id") long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Post post = postService.findPostById(id);
        List<Role> roleList = user.getRoles();
        String userRole = "";
        for (Role role : roleList) {
            if ((role.getName()).equals("admin"))
                userRole = "admin";
        }
        if (userRole.equals("admin")) {
            postService.deletePostById(id);
            return "redirect:/home";
        } else {
            if (isPrincipalOwnerOfPost(principal, post)) {
                postService.deletePostById(id);
                return "redirect:/home";
            } else
                return "redirect:/home?error";
        }
    }

    @GetMapping("/showPost/{id}")
    public String showPost(@PathVariable(value = "id") long id, Model model) {
        Post post = postService.findPostById(id);
        Set<Tag> tags = post.getTags();
        String tagsWithComma = "";
        for (Tag tag : tags) {
            tagsWithComma = tagsWithComma + tag.getName() + ",";
        }
        model.addAttribute("tagsWithComma", tagsWithComma);
        model.addAttribute("post", post);
        return "showPost";
    }

    @PostMapping("/updatePostDetail")
    public ModelAndView updatePostDetail(@ModelAttribute Post post) {
        postService.updatePostById(post);
        return new ModelAndView("redirect:/home");
    }

    private boolean isPrincipalOwnerOfPost(Principal principal, Post post) {
        return principal != null && principal.getName().equals(post.getUser().getEmail());
    }
}
