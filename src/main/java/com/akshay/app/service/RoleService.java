package com.akshay.app.service;

import com.akshay.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
}
