package cotato.timetile.domain.scrap.api.request;

import jakarta.validation.constraints.Size;

public record ScrapFolderUpdateRequest(
        @Size(max = 20, min = 1) String name
) {
}
