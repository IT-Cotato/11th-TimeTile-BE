package cotato.timetile.auth.jwt;

import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AccessTokenReissueController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping(value = "/reissue")
    public ResponseEntity<CommonResponse<?>> reissue(@Valid @RequestBody AccessTokenReissueRequest request) {
        return ApiResponseUtil.success(SuccessResponse.CREATED, refreshTokenService.reissue(request));
    }

}
