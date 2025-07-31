package cotato.timetile.domain.post.api;

import cotato.timetile.domain.post.api.request.PostLoadFileUrlRequest;
import cotato.timetile.domain.post.application.PostFileService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/posts/files")
@RequiredArgsConstructor
@Tag(name = "개인 기록")
public class PostFileController {

    private final PostFileService postFileService;

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('WATCHER', 'LINKER', 'EDITOR', 'ADMIN')")
    @Operation(summary = "업로드 URL 조회")
    public ResponseEntity<CommonResponse<?>> loadUrl(@Valid @RequestBody PostLoadFileUrlRequest request) {
        return ApiResponseUtil.success(SuccessResponse.OK, postFileService.loadUrl(request));
    }

}
