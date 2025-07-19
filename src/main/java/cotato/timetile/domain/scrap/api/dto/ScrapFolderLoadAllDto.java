package cotato.timetile.domain.scrap.api.dto;

import cotato.timetile.domain.scrap.domain.ScrapFolder;

public record ScrapFolderLoadAllDto(
        Long id,
        String name
) {
    public static ScrapFolderLoadAllDto of(ScrapFolder scrapFolder) {
        return new ScrapFolderLoadAllDto(
                scrapFolder.getId(),
                scrapFolder.getName()
        );
    }
}
