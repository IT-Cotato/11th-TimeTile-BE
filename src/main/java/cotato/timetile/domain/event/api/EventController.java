package cotato.timetile.domain.event.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.event.api.request.EventCreationRequest;
import cotato.timetile.domain.event.api.request.EventUpdateRequest;
import cotato.timetile.domain.event.application.EventCreationService;
import cotato.timetile.domain.event.application.EventLoadService;
import cotato.timetile.domain.event.application.EventRemovalService;
import cotato.timetile.domain.event.application.EventUpdateService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.NumberParam;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/events")
@RequiredArgsConstructor
@Tag(name = "이벤트")
public class EventController {

    private final EventLoadService eventLoadService;
    private final EventCreationService eventCreationService;
    private final EventUpdateService eventUpdateService;
    private final EventRemovalService eventRemovalService;

    @GetMapping(value = "/{artistId}/active-years")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "활동 연도 조회")
    public ResponseEntity<CommonResponse<?>> loadHotAndActiveYear(@PathVariable String artistId) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventLoadService.loadHotAndActiveYear(artistId));
    }

    @GetMapping(value = "/{artistId}/hot")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "연도별 대표 조회")
    public ResponseEntity<CommonResponse<?>> loadHotOnYear(@PathVariable String artistId,
                                                           @RequestParam int year) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventLoadService.loadHotOnYear(artistId, year));
    }

    @GetMapping(value = "/{artistId}/more")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "연도 및 월별 추가 조회")
    public ResponseEntity<CommonResponse<?>> loadMore(@PathVariable String artistId,
                                                      @RequestParam int year,
                                                      @RequestParam int month,
                                                      @RequestParam(required = false, defaultValue = NumberParam.DAY_MAX) Integer lastDay,
                                                      @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastEventId) {
        return ApiResponseUtil.success(SuccessResponse.OK,
                eventLoadService.loadMore(artistId, year, month, lastDay, lastEventId));
    }

    @GetMapping(value = "/{groupId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "단일 조회")
    public ResponseEntity<CommonResponse<?>> loadActive(@PathVariable String groupId) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventLoadService.loadActive(groupId));
    }

    @GetMapping(value = "/{groupId}/participants")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "참여자 조회")
    public ResponseEntity<CommonResponse<?>> loadParticipants(@PathVariable String groupId) {
        return ApiResponseUtil.success(SuccessResponse.OK, eventLoadService.loadParticipants(groupId));
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('EDITOR', 'ADMIN')")
    @Operation(summary = "생성")
    public ResponseEntity<CommonResponse<?>> create(@Valid @RequestBody EventCreationRequest request,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        eventCreationService.create(request, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @PutMapping(value = "/{groupId}")
    @PreAuthorize(value = "hasAnyRole('LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "수정")
    public ResponseEntity<CommonResponse<?>> update(@Valid @RequestBody EventUpdateRequest request,
                                                    @PathVariable String groupId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        eventUpdateService.update(request, groupId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @DeleteMapping(value = "/{eventId}")
    @PreAuthorize(value = "hasAnyRole('EDITOR', 'ADMIN')")
    @Operation(summary = "삭제")
    public ResponseEntity<CommonResponse<?>> remove(@PathVariable Long eventId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        eventRemovalService.remove(eventId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
