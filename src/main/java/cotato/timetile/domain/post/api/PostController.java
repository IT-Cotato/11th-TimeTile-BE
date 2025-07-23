package cotato.timetile.domain.post.api;

import cotato.timetile.auth.UserPrincipal;
import cotato.timetile.domain.post.api.request.PostCreationRequest;
import cotato.timetile.domain.post.api.request.PostUpdateRequest;
import cotato.timetile.domain.post.application.PostCreationService;
import cotato.timetile.domain.post.application.PostLoadService;
import cotato.timetile.domain.post.application.PostRemovalService;
import cotato.timetile.domain.post.application.PostUpdateService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.NumberParam;
import cotato.timetile.global.common.SortBy;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
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
@RequestMapping(value = "/api/posts")
@RequiredArgsConstructor
@Tag(name = "개인 기록")
public class PostController {

    private final PostLoadService postLoadService;
    private final PostCreationService postCreationService;
    private final PostUpdateService postUpdateService;
    private final PostRemovalService postRemovalService;

    @GetMapping(value = "/{artistId}/hot")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "연도별 대표 조회")
    public ResponseEntity<CommonResponse<?>> loadHotOnYear(@PathVariable String artistId,
                                                           @RequestParam int year) {
        return ApiResponseUtil.success(SuccessResponse.OK, postLoadService.loadHotOnYear(artistId, year));
    }

    @GetMapping(value = "/{groupId}/more")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "연도 및 월별 추가 조회")
    public ResponseEntity<CommonResponse<?>> loadMore(@PathVariable String groupId,
                                                      @RequestParam(required = false, defaultValue = NumberParam.LONG_MAX) Long lastPostId) {
        return ApiResponseUtil.success(SuccessResponse.OK, postLoadService.loadMore(groupId, lastPostId));
    }

    @GetMapping(value = "/{groupId}/all")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "전체 조회")
    public ResponseEntity<CommonResponse<?>> loadAll(
            @RequestParam(required = false, defaultValue = "LATEST") SortBy sortBy,
            @RequestParam(defaultValue = "1") int page,
            @PathVariable String groupId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = Optional.ofNullable(userPrincipal).map(UserPrincipal::getId).orElse(null);
        return ApiResponseUtil.success(SuccessResponse.OK,
                postLoadService.loadAll(groupId, userId, sortBy, page));
    }

    @GetMapping(value = "/{postId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "상세 조회")
    public ResponseEntity<CommonResponse<?>> load(@PathVariable Long postId) {
        return ApiResponseUtil.success(SuccessResponse.OK, postLoadService.load(postId));
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "생성")
    public ResponseEntity<CommonResponse<?>> create(@Valid @RequestBody PostCreationRequest request,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postCreationService.create(request, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

    @PutMapping(value = "/{postId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "수정")
    public ResponseEntity<CommonResponse<?>> update(@Valid @RequestBody PostUpdateRequest request,
                                                    @PathVariable Long postId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postUpdateService.update(request, postId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

    @DeleteMapping(value = "/{postId}")
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "삭제")
    public ResponseEntity<CommonResponse<?>> remove(@PathVariable Long postId,
                                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postRemovalService.remove(postId, userPrincipal.getId());
        return ApiResponseUtil.success(SuccessResponse.OK);
    }

}
