package cotato.timetile.domain.comment.domain;

import cotato.timetile.domain.comment.api.dto.CommentCreationDto;
import cotato.timetile.domain.comment.api.dto.CommentUpdateDto;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.global.common.TimeInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(length = 500, nullable = false)
    private String content;

    @Embedded
    private TimeInfo timeInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commenter_id", nullable = false)
    private User commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> children = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CommentLike> likes = new ArrayList<>();

    private int likeCount;

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public static Comment of(CommentCreationDto dto) {
        return Comment.builder()
                .commenter(dto.commenter())
                .post(dto.post())
                .parent(dto.parent())
                .content(dto.content())
                .build();
    }

    public boolean isNotWrittenBy(Long userId) {
        return !commenter.getId().equals(userId);
    }

    public void update(CommentUpdateDto dto) {
        this.content = dto.content();
    }

    public boolean isParent() {
        return this.parent == null;
    }

}
