package cotato.timetile.domain.event.api;

import cotato.timetile.domain.event.application.EventSearchService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/events/search")
@RequiredArgsConstructor
@Tag(name = "이벤트")
public class EventSearchController {

    private final EventSearchService eventSearchService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('EDITOR', 'ADMIN')")
    @Operation(summary = "검색")
    public ResponseEntity<CommonResponse<?>> search(@RequestParam String query) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventSearchService.search(query));
    }

}
