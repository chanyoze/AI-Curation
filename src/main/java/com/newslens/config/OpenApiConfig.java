package com.newslens.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 문서 메타. Swagger UI: {@code /swagger-ui.html}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI newsLensOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("NewsLens API")
                .description("외신 AI·테크 뉴스 수집·요약·RAG 검색·실시간 알림 큐레이션 플랫폼")
                .version("v0.0.1"));
    }
}
