package com.newslens.collect;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yml 의 {@code guardian.*} 설정을 타입 안전하게 바인딩한다.
 *
 * <pre>
 * guardian:
 *   api-key: ${GUARDIAN_API_KEY:}      → apiKey
 *   base-url: https://content.guardianapis.com  → baseUrl
 * </pre>
 *
 * <p>케밥케이스({@code api-key}) ↔ 카멜케이스({@code apiKey}) 는 스프링의 relaxed binding 이 자동 매핑한다.
 * record 라서 불변이며, 값은 부팅 시 한 번 주입된다.</p>
 *
 * @param apiKey  Guardian Content API 키 (환경변수 GUARDIAN_API_KEY 로 주입)
 * @param baseUrl API 기본 주소
 */
@ConfigurationProperties(prefix = "guardian")
public record GuardianProperties(String apiKey, String baseUrl) {
}
