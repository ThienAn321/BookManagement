package com.learn.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/admin")
    public String admin() {
        return "hello world !";
    }
    
    @GetMapping("/home")
    public String home() {
        return "hello world !";
    }
    
}
