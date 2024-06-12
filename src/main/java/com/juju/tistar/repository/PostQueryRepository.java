package com.juju.tistar.repository;

import com.juju.tistar.entity.QPost;
import com.juju.tistar.entity.QTag;
import com.juju.tistar.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;
    private final QUser user = QUser.user;
    private final QTag tag = QTag.tag;



}
