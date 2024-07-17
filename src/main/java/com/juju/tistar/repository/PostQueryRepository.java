package com.juju.tistar.repository;

import com.juju.tistar.entity.*;
import com.juju.tistar.entity.enums.SortType;
import com.juju.tistar.response.PostDetailResponse;
import com.juju.tistar.response.PostResponse;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
import java.util.Random;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;
    private final QUser user = QUser.user;
    private final QHeart heart = QHeart.heart;
    private final QImage image = QImage.image;
    private final QReview review = QReview.review;


    public Slice<PostResponse> postList(final Pageable pageable,
                                        final SortType sortType) {

        final ConstructorExpression<PostResponse> postResponse =
                    Projections.constructor(
                        PostResponse.class,
                        post.id,
                        image.imagePath.min(),
                        post.createdAt,
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

    public PostDetailResponse readPost(final Long postId, final Long userId) {
        final ConstructorExpression<PostDetailResponse.Writer> writer = Projections.constructor(
                PostDetailResponse.Writer.class,
                user.name
        );

        PostDetailResponse basicPostDetails = jpaQueryFactory
                .select(Projections.constructor(
                        PostDetailResponse.class,
                        post.id,
                        post.content,
                        writer,
                        post.createdAt,
                        heart.count(),
                        isHeart(postId, userId),
                        Projections.list(new Expression<?>[0]),
                        Projections.list(new Expression<?>[0])
                ))
                .from(post)
                .join(user).on(post.user.eq(user))
                .leftJoin(heart).on(heart.post.eq(post))
                .where(post.id.eq(postId))
                .fetchFirst();

        List<PostDetailResponse.Image> images = jpaQueryFactory
                .select(Projections.constructor(
                        PostDetailResponse.Image.class,
                        image.id,
                        image.fileName,
                        image.imagePath
                ))
                .from(image)
                .where(image.post.id.eq(postId))
                .fetch();

        List<PostDetailResponse.Review> reviews = jpaQueryFactory
                .select(Projections.constructor(
                        PostDetailResponse.Review.class,
                        review.id,
                        review.content,
                        review.user.id,
                        review.user.name
                ))
                .from(review)
                .where(review.post.id.eq(postId))
                .fetch();

        return new PostDetailResponse(
                basicPostDetails.id(),
                basicPostDetails.content(),
                basicPostDetails.writer(),
                basicPostDetails.createdAt(),
                basicPostDetails.heartCount(),
                basicPostDetails.isHeart(),
                images,
                reviews
        );
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
                .leftJoin(heart).on(heart.post.eq(post))
                .leftJoin(post.images, image);
    }
    private OrderSpecifier ordering(final SortType sortType) {
        return switch (sortType) {
            case LATEST -> post.createdAt.desc();
            case HEARTS -> heart.count().desc();
            case OLDEST -> post.createdAt.asc();
            default -> post.id.asc();
        };
    }
}
