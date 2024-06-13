package com.juju.tistar.repository;

import com.juju.tistar.entity.*;
import com.juju.tistar.entity.enums.SortType;
import com.juju.tistar.response.PostDetailResponse;
import com.juju.tistar.response.PostResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;
    private final QUser user = QUser.user;
    private final QHeart heart = QHeart.heart;
    private final QImage image = QImage.image;


    public Slice<PostResponse> postList(final Pageable pageable,
                                              final SortType sortType) {

        final ConstructorExpression<PostResponse> postResponse =
                Projections.constructor(
                        PostResponse.class,
                        post.id,
                        image.imagePath,
                        image.createdAt,
                        heart.count()
                );

        final List<PostResponse> result = getJpaQuery(postResponse)
                .groupBy(post.id)
                .orderBy(ordering(sortType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    public PostDetailResponse readPost(final Long postId, final Long accessId) {

        final ConstructorExpression<PostDetailResponse.Writer> writer =
                Projections.constructor(
                        PostDetailResponse.Writer.class,
                        user.name
                );

        final ConstructorExpression<PostDetailResponse> readPostResponse =
                Projections.constructor(
                        PostDetailResponse.class,
                        user.id,
                        post.content,
                        writer,
                        post.createdAt,
                        heart.count(),
                        isHeart(postId, accessId)
                );

        return
                getJpaQuery(readPostResponse)
                        .where(post.id.eq(postId))
                        .fetchFirst();
    }

    private BooleanExpression isHeart(final Long postId, final Long accessId) {
        return JPAExpressions
                .selectFrom(heart)
                .where(heart.user.id.eq(accessId).and(heart.post.id.eq(postId)))
                .exists();
    }

    private <T> JPAQuery<T> getJpaQuery(final ConstructorExpression<T> constructorExpression) {
        return jpaQueryFactory
                .query()
                .select(constructorExpression)
                .from(post)
                .join(user).on(post.user.eq(user))
                .leftJoin(heart).on(heart.post.eq(post));
    }
    private OrderSpecifier ordering(final SortType sortType) {

        return switch (sortType) {

            case LATEST -> post.createdAt.desc();
            case HEARTS -> heart.count().desc();
            case OLDEST -> post.createdAt.asc();
            default -> NumberExpression.random().asc();
        };
    }
}
