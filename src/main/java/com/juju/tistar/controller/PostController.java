package com.juju.tistar.controller;

import com.juju.tistar.response.PostResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    @PostMapping("/post")
    public String post() {
        return "Post";
    }

    @GetMapping("/home")
    public String home() {
        return "images";
    }

    @GetMapping("/{userId}/myPage")
    public String myPage() {
        return "myImage";
    }
}