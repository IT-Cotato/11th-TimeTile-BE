package cotato.timetile.domain.term.api;

import cotato.timetile.domain.term.application.TermLoadService;
import cotato.timetile.global.common.CommonResponse;
import cotato.timetile.global.common.SuccessResponse;
import cotato.timetile.global.util.ApiResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/terms")
@RequiredArgsConstructor
@Tag(name = "약관")
public class TermController {

    private final TermLoadService termLoadService;

    @GetMapping
    public ResponseEntity<CommonResponse<?>> load() {
        return ApiResponseUtil.success(SuccessResponse.OK, termLoadService.load());
    }

}
