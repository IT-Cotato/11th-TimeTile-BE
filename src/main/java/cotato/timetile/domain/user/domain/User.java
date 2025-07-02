package cotato.timetile.domain.user.domain;

import cotato.timetile.global.common.TimeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Embedded
    private TimeInfo timeInfo;

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

}
