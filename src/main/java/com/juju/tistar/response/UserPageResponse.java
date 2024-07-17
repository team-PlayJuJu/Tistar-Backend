package com.juju.tistar.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponse {
    private String name;
    private String introduction;
    private List<UserPostsResponse> postsResponses;
}
