package cotato.timetile.domain.scrap.domain;

import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.global.common.TimeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "scraps", uniqueConstraints = @UniqueConstraint(columnNames = {"scrap_folder_id", "post_id"}))
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_folder_id")
    private ScrapFolder scrapFolder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private TimeInfo timeInfo;

    public static Scrap of(ScrapFolder scrapFolder, Post post) {
        return Scrap.builder()
                .scrapFolder(scrapFolder)
                .post(post)
                .build();
    }

}
