package cotato.timetile.domain.user.api;

import cotato.timetile.domain.user.api.request.UserLoadFileUrlRequest;
import cotato.timetile.domain.user.application.UserFileService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users/files")
@RequiredArgsConstructor
@Tag(name = "유저")
public class UserFileController {

    private final UserFileService userFileService;

    @PostMapping
    @Operation(summary = "업로드 URL 조회")
    public ResponseEntity<CommonResponse<?>> loadUrl(@Valid @RequestBody UserLoadFileUrlRequest request) {
        return ApiResponseUtil.success(SuccessResponse.OK, userFileService.loadUrl(request));
    }

}
