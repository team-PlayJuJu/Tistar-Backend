package com.juju.tistar.mapper;


import com.juju.tistar.entity.Heart;
import com.juju.tistar.entity.Post;
import com.juju.tistar.entity.User;
import com.juju.tistar.response.HeartResponse;

public class HeartMapper {
    static public Heart HeartRequest(final User user, final Post post) {

        return new Heart(user,post);
    }

    static public HeartResponse heartResponse(final Heart heart) {

        return new HeartResponse(heart.getId());
    }
}