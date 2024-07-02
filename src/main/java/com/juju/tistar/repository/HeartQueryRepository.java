package com.juju.tistar.repository;

import com.juju.tistar.entity.QHeart;
import com.juju.tistar.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HeartQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QHeart heart = QHeart.heart;
    private final QPost post = QPost.post;

    public Long countByPostId(final Long postId) {

        return
                jpaQueryFactory
                        .select(heart.count())
                        .from(post)
                        .leftJoin(heart).on(heart.post.eq(post))
                        .where(post.id.eq(postId))
                        .fetchFirst();
    }
}
