package com.juju.tistar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    @PostMapping("/post")
    public String post() {
        return "imagePost";
    }

    @GetMapping("/home")
    public String home() {
        System.out.println("ang");
        return "images";
    }

    @GetMapping("/{userId}/myPage")
    public String myPage() {
        return "myImage";
    }
}