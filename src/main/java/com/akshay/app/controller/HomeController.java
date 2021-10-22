package com.akshay.app.controller;

import com.akshay.app.model.Post;
import com.akshay.app.model.Tag;
import com.akshay.app.repository.TagRepository;
import com.akshay.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @Autowired
    private TagRepository tagRepository;


    @GetMapping("/home")
    public String showHomePage(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                               @RequestParam(value = "size", required = false) Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Post> posts = postService.getPage(pageable);

        List<String> distinctAuthorName = postService.findDistinctAuthor();
        List<String> distinctAuthorTag = tagRepository.findDistinctTag();

        model.addAttribute("distinctAuthorName", distinctAuthorName);
        model.addAttribute("distinctAuthorTag", distinctAuthorTag);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", posts.getTotalPages());
        return "home";
    }

    @GetMapping("/home/sortDesc")
    public String sortTimeInDescendingOrder(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                            Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Post> posts = postService.sortByPublishedAtDesc(pageable);
        List<String> distinctAuthorName = postService.findDistinctAuthor();
        List<String> distinctAuthorTag = tagRepository.findDistinctTag();

        model.addAttribute("distinctAuthorName", distinctAuthorName);
        model.addAttribute("distinctAuthorTag", distinctAuthorTag);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", posts.getTotalPages());
        return "home";
    }

    @GetMapping("/home/sortAsc")
    public String sortTimeInAscendingOrder(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                           Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Post> posts = postService.sortByPublishedAt(pageable);
        List<String> distinctAuthorName = postService.findDistinctAuthor();
        List<String> distinctAuthorTag = tagRepository.findDistinctTag();

        model.addAttribute("distinctAuthorName", distinctAuthorName);
        model.addAttribute("distinctAuthorTag", distinctAuthorTag);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", posts.getTotalPages());
        return "home";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam(value = "author", required = false, defaultValue = "") String[] selectedAuthors,
                         @RequestParam(value = "tag", required = false, defaultValue = "") String[] selectedTags,
                         @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                         Model model) {

        if (selectedAuthors.length == 0 && selectedTags.length == 0 && startDate.equals("") && endDate.equals("")) {
            return "redirect:/home";
        }
        List<Post> postList = new ArrayList<>();
        Set<Post> tagList = new HashSet<>();
        if (selectedAuthors.length != 0) {
            for (String author : selectedAuthors) {
                List<Post> postListOfSingleAuthor = postService.findByAuthor(author);
                for (Post post : postListOfSingleAuthor) {
                    postList.add(post);
                }
            }
        } else {
            postList = postService.findAll();
        }
        if (selectedTags.length != 0) {
            for (String name : selectedTags) {
                for (Post post : postList) {
                    for (Tag tag : post.getTags()) {
                        if (tag.getName().equals(name)) {
                            tagList.add(post);
                        }
                    }
                }
            }
            postList.clear();
            postList.addAll(tagList);
        }
        List<Post> postListWithTime = new ArrayList<>();
        for (Post post : postList) {
            if (post.getPublishedAt().compareTo(startDate) >= 0) {
                if (post.getPublishedAt().compareTo(endDate) <= 0)
                    postListWithTime.add(post);
            }
        }
        if (((selectedAuthors.length != 0) || (selectedTags.length != 0)) && startDate.isEmpty()) {
            model.addAttribute("posts", postList);
        } else if (((selectedAuthors.length != 0) || (selectedTags.length != 0)) && !(startDate.isEmpty())) {
            model.addAttribute("posts", postListWithTime);
        } else if (((selectedAuthors.length != 0) && selectedTags.length != 0) && !(startDate.isEmpty())) {
            model.addAttribute("posts", postListWithTime);
        } else if (!(startDate.isEmpty()) && ((selectedAuthors.length == 0) && (selectedTags.length == 0))) {
            model.addAttribute("posts", postListWithTime);
        } else {
            model.addAttribute("posts", postList);
        }

        List<String> distinctAuthorName = postService.findDistinctAuthor();
        List<String> distinctAuthorTag = tagRepository.findDistinctTag();
        model.addAttribute("distinctAuthorName", distinctAuthorName);
        model.addAttribute("distinctAuthorTag", distinctAuthorTag);
        return "home2";
    }

    @GetMapping("/home/search")
    public String search(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                         Model model) {
        String keyword = request.getParameter("tagss");
        Pageable pageable = PageRequest.of(page, 10);
        Page<Post> posts = postService.listAllPost(keyword, pageable);
        List<String> distinctAuthorName = postService.findDistinctAuthor();
        List<String> distinctAuthorTag = tagRepository.findDistinctTag();
        model.addAttribute("posts", posts);
        model.addAttribute("distinctAuthorName", distinctAuthorName);
        model.addAttribute("distinctAuthorTag", distinctAuthorTag);
        return "home2";
    }
}
