package com.newslens.collect.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Guardian Content API {@code /search} 응답 매핑 DTO.
 *
 * <p>실제 응답 구조(발췌):</p>
 * <pre>
 * {
 *   "response": {
 *     "status": "ok", "total": 60086, "pageSize": 3, "currentPage": 1, "pages": 20029,
 *     "results": [
 *       {
 *         "id": "technology/2026/jun/30/...",   // ← 중복 판별 키(source_id)
 *         "type": "article",
 *         "sectionName": "Technology",
 *         "webPublicationDate": "2026-06-30T19:51:07Z",
 *         "webTitle": "...",                     // ← headline 없을 때 제목 fallback
 *         "webUrl": "https://www.theguardian.com/...",  // ← 원문 링크(url)
 *         "fields": { "headline": "...", "byline": "...", "trailText": "...", "bodyText": "..." }
 *       }
 *     ]
 *   }
 * }
 * </pre>
 *
 * <p>{@code @JsonIgnoreProperties(ignoreUnknown = true)} — 응답에 우리가 안 쓰는 필드가 많으므로
 * 모르는 필드는 조용히 무시한다(그게 없으면 파싱 자체가 실패).</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GuardianResponse(Response response) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            String status,
            int total,
            int pageSize,
            int currentPage,
            int pages,
            List<Result> results
    ) {}

    /** 기사 1건. liveblog/gallery 등 타입에 따라 {@code fields} 가 비어 올 수 있어 null 허용. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            String id,
            String type,
            String sectionName,
            String webPublicationDate,
            String webTitle,
            String webUrl,
            Fields fields
    ) {
        /** headline 이 있으면 그걸, 없으면 webTitle 을 원문 제목으로 쓴다. */
        public String resolvedTitle() {
            return (fields != null && fields.headline() != null) ? fields.headline() : webTitle;
        }

        /** 본문(없을 수 있음). */
        public String bodyText() {
            return fields != null ? fields.bodyText() : null;
        }
    }

    /** show-fields 로 추가 요청한 필드들. 타입에 따라 일부/전부 null 가능. */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Fields(
            String headline,
            String byline,
            String trailText,
            String bodyText
    ) {}
}
