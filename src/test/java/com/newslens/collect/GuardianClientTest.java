package com.newslens.collect;

import com.newslens.collect.dto.GuardianResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guardian 수집 클라이언트 실동작 검증.
 *
 * <p>실제 Guardian API 를 호출하므로 인터넷 + 유효한 키가 필요하다.
 * {@code @EnabledIfEnvironmentVariable} 로 GUARDIAN_API_KEY 가 있을 때만 실행 → 키 없는 환경에선 자동 skip.</p>
 *
 * <p>{@code webEnvironment = NONE} — 웹 서버는 안 띄우고 스프링 컨텍스트만 올린다
 * (컨텍스트가 뜨면서 GuardianProperties 바인딩 + GuardianClient 주입까지 실제로 검증된다).</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnabledIfEnvironmentVariable(named = "GUARDIAN_API_KEY", matches = ".+")
class GuardianClientTest {

    private static final Logger log = LoggerFactory.getLogger(GuardianClientTest.class);

    @Autowired
    GuardianClient guardianClient;

    @Test
    void fetchLatest_technology_returnsArticles() {
        List<GuardianResponse.Result> results = guardianClient.fetchLatest("technology", 5);

        assertThat(results).isNotEmpty();
        results.forEach(r -> log.info("[{}] {}  ->  {}",
                r.webPublicationDate(), r.resolvedTitle(), r.webUrl()));

        // 매핑이 제대로 되는지 첫 건으로 핵심 필드 점검
        GuardianResponse.Result first = results.get(0);
        assertThat(first.id()).isNotBlank();        // source_id (중복키)
        assertThat(first.webUrl()).startsWith("https://www.theguardian.com");
        assertThat(first.resolvedTitle()).isNotBlank();
    }
}
