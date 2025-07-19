package cotato.timetile.domain.scrap.api.dto;

import cotato.timetile.domain.scrap.domain.ScrapFolder;

public record ScrapStatusLoadDto(
        Long scrapFolderId,
        String scrapFolderName,
        boolean isScrapped
) {
    public static ScrapStatusLoadDto of(ScrapFolder scrapFolder, Long postId) {
        return new ScrapStatusLoadDto(
                scrapFolder.getId(),
                scrapFolder.getName(),
                scrapFolder.alreadyScrapped(postId)
        );
    }
}
