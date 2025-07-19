package cotato.timetile.domain.event.domain;

import cotato.timetile.domain.artist.domain.Artist;
import cotato.timetile.domain.event.api.dto.EventCreationDto;
import cotato.timetile.domain.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import jakarta.persistence.OrderBy;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "events")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String groupId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String description;

    @Column(nullable = false)
    private String source;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "savedOrder ASC")
    private final List<RelatedArtist> relatedArtists = new ArrayList<>();

    @ElementCollection(targetClass = ActivityType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "event_activity_types", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "activity_type", nullable = false)
    @OrderColumn(name = "saved_order")
    private List<ActivityType> activityTypes;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "event_related_events", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "related_event_group_id")
    @OrderColumn(name = "saved_order")
    private List<String> relatedEvents;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "event_related_materials", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "related_material")
    @OrderColumn(name = "saved_order")
    private List<String> relatedMaterials;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate endedAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime editedAt;

    private int reportCount;

    private int postCount;

    private int contributorCount;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeType changeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public static Event of(EventCreationDto dto) {
        return Event.builder()
                .groupId(dto.groupId())
                .name(dto.name())
                .description(dto.description())
                .source(dto.source())
                .activityTypes(dto.activityTypes())
                .relatedEvents(dto.relatedEvents())
                .relatedMaterials(dto.relatedMaterials())
                .startedAt(dto.startedAt())
                .endedAt(dto.endedAt())
                .editedAt(LocalDateTime.now())
                .contributorCount(dto.contributorCount())
                .changeType(dto.changeType())
                .postCount(dto.postCount())
                .author(dto.author())
                .build();
    }

    public void increaseReportCount() {
        this.reportCount++;
    }

    public void decreaseReportCount() {
        this.reportCount--;
    }

    public void increasePostCount() {
        this.postCount++;
    }

    public void decreasePostCount() {
        this.postCount--;
    }

    public void activate() {
        this.active = true;
    }

    public void addRelatedArtist(Artist artist) {
        this.relatedArtists.add(RelatedArtist.of(this, artist, this.relatedArtists.size()));
    }

    public void isAbout(Artist artist) {
        this.artist = artist;
    }

    public boolean isNotWrittenBy(User author) {
        return !this.author.equals(author);
    }

}
