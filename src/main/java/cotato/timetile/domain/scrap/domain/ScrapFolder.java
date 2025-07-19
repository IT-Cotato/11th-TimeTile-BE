package cotato.timetile.domain.scrap.domain;

import cotato.timetile.domain.scrap.api.dto.ScrapFolderCreationDto;
import cotato.timetile.domain.scrap.api.dto.ScrapFolderUpdateDto;
import cotato.timetile.domain.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

@Table(name = "scrap_folders")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ScrapFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User creator;

    @OneToMany(mappedBy = "scrapFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    public static ScrapFolder of(ScrapFolderCreationDto dto) {
        return ScrapFolder.builder()
                .name(dto.name())
                .creator(dto.creator())
                .build();
    }

    public void update(ScrapFolderUpdateDto dto) {
        this.name = dto.name();
    }

    public boolean isNotCreatedBy(Long userId) {
        return !creator.getId().equals(userId);
    }

    public void addScrap(Scrap scrap) {
        this.scraps.add(scrap);
    }

    public void removeScrap(Scrap scrap) {
        this.scraps.remove(scrap);
    }

    public void clearScrap() {
        this.scraps.clear();
    }

    public boolean alreadyScrapped(Long postId) {
        return !scraps.stream().filter(scrap -> scrap.getPost().getId().equals(postId)).toList().isEmpty();
    }

}
