package cotato.timetile.domain.profile.api.dto;

public record FollowProfileDto(
        String id,
        String name,
        String profileImageUrl
) {
    public static FollowProfileDto of(String id, String name, String profileImageUrl) {
        return new FollowProfileDto(id, name, profileImageUrl);
    }
}
