package com.akshay.app.service;

import com.akshay.app.model.Tag;
import com.akshay.app.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag save(Tag tag){
        return tagRepository.save(tag);
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }

    public List<String> findDistinctTag(){
        return tagRepository.findDistinctTag();
    }
}
