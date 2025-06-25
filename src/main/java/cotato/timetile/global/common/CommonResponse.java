package cotato.timetile.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private final T data;

    private CommonResponse(Builder<T> builder) {
        this.isSuccess = builder.isSuccess;
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    public static CommonResponse<?> success(SuccessResponse successResponse) {
        return builder()
                .isSuccess(true)
                .code(successResponse.getCode())
                .message(successResponse.getMessage())
                .build();
    }

    public static <T> CommonResponse<?> success(SuccessResponse successResponse, T data) {
        return builder()
                .isSuccess(true)
                .code(successResponse.getCode())
                .message(successResponse.getMessage())
                .data(data)
                .build();
    }

    public static CommonResponse<?> error(ErrorResponse errorResponse) {
        return builder()
                .isSuccess(false)
                .code(errorResponse.getCode())
                .message(errorResponse.getMessage())
                .build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Boolean isSuccess;
        private String code;
        private String message;
        private T data;

        public Builder<T> isSuccess(Boolean isSuccess) {
            this.isSuccess = isSuccess;
            return this;
        }

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public CommonResponse<T> build() {
            return new CommonResponse<>(this);
        }
    }
}
