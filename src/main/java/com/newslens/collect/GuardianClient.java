package com.newslens.collect;

import com.newslens.collect.dto.GuardianResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Guardian Content API 호출 클라이언트.
 *
 * <p>"외신 뉴스 한 페이지를 긁어온다" 는 한 가지 책임만 진다. (중복제거·저장·LLM 은 이후 단계의 다른 컴포넌트가 담당)</p>
 *
 * <p>{@link RestClient} 는 스프링 6의 동기 HTTP 클라이언트로, {@code RestClient.Builder} 는
 * spring-boot-starter-web 이 자동 구성해 주입해 준다. 여기서 baseUrl 만 고정해 재사용한다.</p>
 */
@Component
public class GuardianClient {

    private static final Logger log = LoggerFactory.getLogger(GuardianClient.class);

    private final RestClient restClient;
    private final GuardianProperties props;

    public GuardianClient(GuardianProperties props, RestClient.Builder builder) {
        this.props = props;
        this.restClient = builder.baseUrl(props.baseUrl()).build();
    }

    /**
     * 특정 섹션의 최신 기사들을 가져온다.
     *
     * @param section  Guardian 섹션 (예: {@code "technology"})
     * @param pageSize 가져올 기사 수 (Developer 티어 한도 고려)
     * @return 기사 목록 (없거나 응답 이상 시 빈 리스트)
     */
    public List<GuardianResponse.Result> fetchLatest(String section, int pageSize) {
        GuardianResponse body = restClient.get()
                .uri(uri -> uri
                        .path("/search")
                        .queryParam("section", section)
                        .queryParam("order-by", "newest")
                        .queryParam("page-size", pageSize)
                        // 번역·요약·임베딩에 필요한 원문 필드를 함께 요청
                        .queryParam("show-fields", "headline,trailText,bodyText,byline")
                        .queryParam("api-key", props.apiKey())
                        .build())
                .retrieve()
                .body(GuardianResponse.class);

        if (body == null || body.response() == null || body.response().results() == null) {
            log.warn("Guardian 응답이 비었습니다. section={}", section);
            return List.of();
        }

        List<GuardianResponse.Result> results = body.response().results();
        log.info("Guardian 수집: section={}, 총 {}건 중 {}건 수신", section, body.response().total(), results.size());
        return results;
    }
}
