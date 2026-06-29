package com.newslens.common.event;

import java.util.List;

/**
 * 새 기사 수집 완료 이벤트 (업무 가이드 3-3 계약).
 *
 * <p>발행: 수집 배치가 새 기사 저장 완료 후 {@code ApplicationEventPublisher} 로 발행.<br>
 * 소비: ① SSE 로 구독자에게 {@code new-articles} 푸시 ② feed/categories Redis 캐시 무효화.</p>
 *
 * @param count      이번에 새로 저장된 기사 수
 * @param articleIds 새로 저장된 기사 id 목록
 */
public record NewArticlesEvent(int count, List<Long> articleIds) {
}
