package cotato.timetile.domain.artist.domain;

import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.global.common.TimeInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

@Table(name = "artists")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Artist {

    @Id
    @Column(updatable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "CHAR(64)", unique = true)
    private String imageUrl;

    private int followCount;

    @Embedded
    private TimeInfo timeInfo;

    @OneToMany(mappedBy = "artist")
    private final List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "artist")
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ArtistFollow> followed = new ArrayList<>();

    public void update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void isSubjectOf(Event event) {
        this.events.add(event);
        event.isAbout(this);
    }

    public void isSubjectOf(Post post) {
        this.posts.add(post);
        post.isAbout(this);
    }

    public void increaseFollowCount() {
        this.followCount++;
    }

    public void decreaseFollowCount() {
        this.followCount--;
    }

}
