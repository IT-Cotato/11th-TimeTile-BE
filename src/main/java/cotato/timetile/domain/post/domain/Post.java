package cotato.timetile.domain.post.domain;

import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.comment.domain.Comment;
import cotato.timetile.domain.post.api.dto.PostCreationDto;
import cotato.timetile.domain.post.api.dto.PostUpdateDto;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.global.common.TimeInfo;
import cotato.timetile.global.common.Visibility;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "posts")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @ElementCollection(targetClass = String.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "post_media_keys", joinColumns = @JoinColumn(name = "post_id"))
    @Column(columnDefinition = "CHAR(64)", unique = true)
    @OrderColumn(name = "media_order")
    private final List<String> mediaKeys = new ArrayList<>();

    @Column(columnDefinition = "CHAR(64)")
    private String mainImageKey;

    @Column(nullable = false, updatable = false)
    private String groupId;

    @Embedded
    private TimeInfo timeInfo;

    @Column(nullable = false)
    private LocalDate startedAt;

    private int likeCount;

    private int commentCount;

    private int scrapCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    public static Post of(PostCreationDto dto) {
        Post post = Post.builder()
                .title(dto.title())
                .content(dto.content())
                .groupId(dto.groupId())
                .visibility(dto.visibility())
                .mainImageKey(dto.mainImageKey())
                .startedAt(dto.startedAt())
                .author(dto.author())
                .build();
        dto.mediaKeys().forEach(post::addMediaKey);
        return post;
    }

    public void update(PostUpdateDto dto) {
        this.title = dto.title();
        this.content = dto.content();
        this.visibility = dto.visibility();
        this.mainImageKey = dto.mainImageKey();
        this.clearMediaKeys();
        dto.mediaKeys().forEach(this::addMediaKey);
    }

    public void isAbout(Artist artist) {
        this.artist = artist;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void increaseScrapCount() {
        this.scrapCount++;
    }

    public void decreaseScrapCount() {
        this.scrapCount--;
    }

    public boolean isNotWrittenBy(User author) {
        return !this.author.equals(author);
    }

    public void addMediaKey(String mediaKey) {
        this.mediaKeys.add(mediaKey);
    }

    public void clearMediaKeys() {
        this.mediaKeys.clear();
    }

}
