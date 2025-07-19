package cotato.timetile.domain.post.application;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cotato.timetile.domain.post.api.dto.PostLoadAllDto;
import cotato.timetile.domain.post.api.dto.PostLoadHotOnYearDto;
import cotato.timetile.domain.post.api.dto.PostLoadMoreDto;
import cotato.timetile.domain.post.api.response.PostLoadAllResponse;
import cotato.timetile.domain.post.api.response.PostLoadHotOnYearResponse;
import cotato.timetile.domain.post.api.response.PostLoadMoreResponse;
import cotato.timetile.domain.post.api.response.PostLoadResponse;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.QPost;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.domain.user.domain.QUserFollow;
import cotato.timetile.global.common.SortBy;
import cotato.timetile.global.common.Visibility;
import cotato.timetile.global.exception.NotFoundException;
import cotato.timetile.global.handler.S3Handler;
import cotato.timetile.global.util.QuerydslUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLoadService {

    private static final int SLICE_SIZE = 5;
    private static final int PAGE_SIZE = 20;
    private final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public PostLoadHotOnYearResponse loadHotOnYear(String artistId, int year) {
        return PostLoadHotOnYearResponse.of(
                postRepository.findTop2PostsPerMonthByArtistAndYear(artistId, year).stream()
                        .map(post -> PostLoadHotOnYearDto.of(
                                post,
                                s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey())
                        ))
                        .collect(Collectors.groupingBy(post -> post.startedAt().getMonthValue()))
        );
    }

    @Transactional(readOnly = true)
    public PostLoadMoreResponse loadMore(String groupId, Long lastPostId) {
        QPost p = QPost.post;
        BooleanExpression baseCondition = p.groupId.eq(groupId)
                .and(p.author.visibility.eq(Visibility.PUBLIC))
                .and(p.visibility.eq(Visibility.PUBLIC));
        BooleanExpression cursorCondition = p.id.lt(lastPostId);
        List<Post> fetched = jpaQueryFactory
                .selectFrom(p)
                .where(baseCondition, cursorCondition)
                .orderBy(p.id.desc())
                .limit(SLICE_SIZE + 1)
                .fetch();
        boolean hasNext = fetched.size() > SLICE_SIZE;
        List<Post> posts = hasNext ? fetched.subList(0, SLICE_SIZE) : fetched;
        Optional<Post> lastPostOpt = posts.isEmpty() ? Optional.empty() : Optional.of(posts.get(posts.size() - 1));
        return PostLoadMoreResponse.of(
                posts.stream().map(post -> PostLoadMoreDto.of(
                        post,
                        s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                        s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey())
                )).toList(),
                hasNext,
                lastPostOpt.map(Post::getId).orElse(null)
        );
    }

    @Transactional(readOnly = true)
    public PostLoadAllResponse loadAll(String groupId, Long userId, SortBy sortBy, int page) {
        QPost post = QPost.post;
        QUserFollow follow = QUserFollow.userFollow;
        BooleanExpression isAuthor = QuerydslUtil.safeEq(post.author.id, userId);
        BooleanExpression isPublicPost = post.visibility.eq(Visibility.PUBLIC);
        BooleanExpression isPublicUser = post.author.visibility.eq(Visibility.PUBLIC);
        BooleanExpression isFollower = JPAExpressions.selectOne()
                .from(follow)
                .where(
                        QuerydslUtil.safeEq(follow.following.id, userId),
                        follow.follower.id.eq(post.author.id)
                ).exists();
        BooleanBuilder isReadable = buildReadableCondition(isAuthor, isPublicPost, isPublicUser, isFollower);
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy);
        PageRequest pageable = PageRequest.of(page - 1, PAGE_SIZE);
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .join(post.author).fetchJoin()
                .where(isReadable.and(post.groupId.eq(groupId)))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(isReadable)
                .fetchOne();
        PageImpl<Post> postPage = new PageImpl<>(posts, pageable, total);
        return PostLoadAllResponse.of(
                postPage.stream().map(p -> PostLoadAllDto.of(
                        p,
                        s3Handler.getSimpleLogoUrlIfNull(p.getMainImageKey()),
                        s3Handler.getSimpleLogoUrlIfNull(p.getAuthor().getImageKey())
                )).toList(),
                postPage
        );
    }

    @Transactional(readOnly = true)
    public PostLoadResponse load(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundException::wrong);
        return PostLoadResponse.of(
                post,
                s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey()),
                post.getMediaKeys().stream().map(s3Handler::generateSignedGetUrl).toList()
        );
    }

    private OrderSpecifier<?> getOrderSpecifier(SortBy sortBy) {
        QPost post = QPost.post;
        return switch (sortBy) {
            case LATEST -> post.timeInfo.createdAt.desc();
            case HOTTEST -> post.likeCount.desc();
        };
    }

    private BooleanBuilder buildReadableCondition(BooleanExpression isAuthor, BooleanExpression isPublicPost,
                                                  BooleanExpression isPublicUser, BooleanExpression isFollower) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isAuthor != null) {
            booleanBuilder.or(isAuthor);
        }
        booleanBuilder.or(isPublicPost.and(isPublicUser.or(isFollower)));
        return booleanBuilder;
    }

}
