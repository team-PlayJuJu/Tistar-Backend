package com.juju.tistar.service;

import com.amazonaws.services.s3.AmazonS3;
import com.juju.tistar.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AmazonS3 amazonS3;
    private final PostRepository postRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


}
