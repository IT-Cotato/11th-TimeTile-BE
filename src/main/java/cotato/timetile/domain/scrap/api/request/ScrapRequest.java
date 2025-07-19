package cotato.timetile.domain.scrap.api.request;

import java.util.List;

public record ScrapRequest(
        List<Long> scrapFolderIds
) {
}
