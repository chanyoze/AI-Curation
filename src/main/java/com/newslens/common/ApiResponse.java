package com.newslens.common;

import java.time.Instant;

/**
 * 전 API 공통 응답 래퍼. 성공/실패를 일관된 형태로 감싼다.
 *
 * @param success 처리 성공 여부
 * @param data    페이로드(실패 시 null)
 * @param error   에러 메시지(성공 시 null)
 * @param timestamp 응답 시각(UTC)
 */
public record ApiResponse<T>(boolean success, T data, String error, Instant timestamp) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> fail(String error) {
        return new ApiResponse<>(false, null, error, Instant.now());
    }
}
