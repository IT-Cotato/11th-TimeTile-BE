package cotato.timetile.domain.user.api;

import cotato.timetile.domain.user.api.request.OAuth2SignupRequest;
import cotato.timetile.domain.user.application.OAuth2SignupService;
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
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@Tag(name = "OAuth2 회원 가입")
public class OAuth2SignupController {

    private final OAuth2SignupService oAuth2SignupService;

    @PostMapping("/oauth2/signup")
    @Operation(summary = "회원 가입")
    public ResponseEntity<CommonResponse<?>> signup(@Valid @RequestBody OAuth2SignupRequest request) {
        oAuth2SignupService.register(request);
        return ApiResponseUtil.success(SuccessResponse.CREATED);
    }

}
