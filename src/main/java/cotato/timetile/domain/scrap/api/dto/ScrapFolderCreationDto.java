package cotato.timetile.domain.scrap.api.dto;

import cotato.timetile.domain.scrap.api.request.ScrapFolderCreationRequest;
import cotato.timetile.domain.user.domain.User;

public record ScrapFolderCreationDto(
        String name,
        User creator
) {
    public static ScrapFolderCreationDto of(ScrapFolderCreationRequest request, User creator) {
        return new ScrapFolderCreationDto(
                request.name(),
                creator
        );
    }
}
