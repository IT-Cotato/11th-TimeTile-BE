package cotato.timetile.domain.event.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.event.application.EventLoadService;
import cotato.timetile.domain.event.application.EventReportService;
import cotato.timetile.domain.event.domain.ChangeType;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/events/pending")
@RequiredArgsConstructor
@Tag(name = "업로드 대기 이벤트")
public class EventPendingController {

    private final EventLoadService eventLoadService;
    private final EventReportService eventReportService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "조회")
    public ResponseEntity<CommonResponse<?>> loadPending(@RequestParam(required = false) ChangeType changeType,
                                                         @RequestParam(defaultValue = "1") int page) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventLoadService.loadPending(changeType, page));
    }

    @PostMapping(value = "/{eventId}/report")
    @PreAuthorize(value = "hasAnyRole('LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "신고")
    public ResponseEntity<CommonResponse<?>> report(@PathVariable Long eventId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        eventReportService.report(eventId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @DeleteMapping(value = "/{eventId}/report")
    @PreAuthorize(value = "hasAnyRole('LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "신고 취소")
    public ResponseEntity<CommonResponse<?>> cancelReport(@PathVariable Long eventId,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        eventReportService.cancelReport(eventId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
