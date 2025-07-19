package cotato.timetile.domain.scrap.api.response;

import cotato.timetile.domain.scrap.api.dto.ScrapFolderLoadAllDto;
import java.util.List;

public record ScrapFolderLoadAllResponse(
        List<ScrapFolderLoadAllDto> scrapFolders
) {
    public static ScrapFolderLoadAllResponse of(List<ScrapFolderLoadAllDto> scrapFolders) {
        return new ScrapFolderLoadAllResponse(scrapFolders);
    }
}
