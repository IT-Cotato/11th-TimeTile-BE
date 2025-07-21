package cotato.timetile.domain.user.domain;

import cotato.timetile.domain.artist.domain.ArtistFollow;
import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.comment.domain.CommentLike;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.PostLike;
import cotato.timetile.domain.scrap.domain.ScrapFolder;
import cotato.timetile.domain.user.api.dto.UserCreationDto;
import cotato.timetile.global.common.TimeInfo;
import cotato.timetile.global.common.Visibility;
import cotato.timetile.global.util.EncryptUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Table(name = "users")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(length = 60)
    private String password;

    @Column(length = 12, nullable = false, unique = true)
    private String nickname;

    @Column(length = 30)
    private String introduction;

    @Column(length = 50, unique = true)
    private String imageKey;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(7)", nullable = false)
    private AuthProvider provider;

    @Column(unique = true)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(7)", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Embedded
    private TimeInfo timeInfo;

    private int followingCount;

    private int followerCount;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ScrapFolder> scrapFolders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ArtistFollow> artistFollows = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserTermAgreement> agreements = new ArrayList<>();

    public static User of(UserCreationDto dto) {
        return User.builder()
                .email(dto.email())
                .password(dto.password() != null ? EncryptUtil.encrypt(dto.password()) : null)
                .nickname(dto.nickname())
                .introduction(dto.introduction())
                .imageKey(dto.imageKey())
                .provider(dto.provider())
                .providerId(dto.providerId())
                .role(Role.WATCHER)
                .visibility(Visibility.PUBLIC)
                .build();
    }

    public void updateProfile(String nickname, String introduction, String imageKey) {
        if (StringUtils.hasText(nickname)) {
            this.nickname = nickname;
        }
        if (StringUtils.hasText(introduction)) {
            this.introduction = introduction;
        }
        if (StringUtils.hasText(imageKey)) {
            this.imageKey = imageKey;
        }
    }

    public void write(Event event) {
        this.events.add(event);
    }

    public void delete(Event event) {
        this.events.remove(event);
    }

    public void write(Post post) {
        this.posts.add(post);
    }

    public void delete(Post post) {
        this.posts.remove(post);
    }

    public void like(PostLike postLike) {
        this.postLikes.add(postLike);
    }

    public void like(CommentLike commentLike) {
        this.commentLikes.add(commentLike);
    }

    public void cancelLike(PostLike postLike) {
        this.postLikes.remove(postLike);
    }

    public void cancelLike(CommentLike commentLike) {
        this.commentLikes.remove(commentLike);
    }

    public void comment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void createScrapFolder(ScrapFolder scrapFolder) {
        this.scrapFolders.add(scrapFolder);
    }

    public void removeScrapFolder(ScrapFolder scrapFolder) {
        this.scrapFolders.remove(scrapFolder);
    }

    public void follow(ArtistFollow artistFollow) {
        this.artistFollows.add(artistFollow);
    }

    public void unfollow(ArtistFollow artistFollow) {
        this.artistFollows.remove(artistFollow);
    }

    public void agree(UserTermAgreement userTermAgreement) {
        this.agreements.add(userTermAgreement);
    }

    public void increaseFollowingCount() {
        this.followingCount++;
    }

    public void decreaseFollowingCount() {
        this.followingCount--;
    }

    public void increaseFollowerCount() {
        this.followerCount++;
    }

    public void decreaseFollowerCount() {
        this.followerCount--;
    }

}
