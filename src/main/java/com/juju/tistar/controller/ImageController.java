package com.juju.tistar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    @PostMapping("/post")
    public String post() {
        return "imagePost";
    }

    @GetMapping("/home")
    public String home() {
        return "image";
    }

    @GetMapping("/{userId}/myPage")
    public String myPage() {
        return "myImage";
    }
}
