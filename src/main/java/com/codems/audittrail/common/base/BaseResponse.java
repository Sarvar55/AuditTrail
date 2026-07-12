package com.codems.audittrail.common.base;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;

    public static <T> BaseResponse<T> success(T data, HttpStatus status) {
        return success(data, status, null);
    }

    public static <T> BaseResponse<T> success(T data, HttpStatus status, String message) {
        return BaseResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> BaseResponse<T> success(T data) {
        return success(data, HttpStatus.OK);
    }

    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> error(T data, HttpStatus status) {
        return error(data, status, null);
    }

    public static <T> BaseResponse<T> error(T data, HttpStatus status, String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .data(data)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
