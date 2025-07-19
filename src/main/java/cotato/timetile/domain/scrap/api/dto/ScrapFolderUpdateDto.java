package cotato.timetile.domain.scrap.api.dto;

import cotato.timetile.domain.scrap.api.request.ScrapFolderUpdateRequest;

public record ScrapFolderUpdateDto(
        String name
) {
    public static ScrapFolderUpdateDto of(ScrapFolderUpdateRequest request) {
        return new ScrapFolderUpdateDto(request.name());
    }
}
