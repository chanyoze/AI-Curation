# NewsLens — AI 미디어 큐레이션 플랫폼

외신 AI·테크 뉴스를 자동 수집해 **LLM으로 한국어 번역·요약·태깅**하고, **자연어 RAG 검색**과
**실시간 알림(SSE)** 을 제공하는 백엔드 플랫폼.

## 스택

| 영역 | 기술 |
|---|---|
| 런타임 | Spring Boot 3.5 / Java 21 |
| DB | PostgreSQL 16 + pgvector (`vector(768)`, HNSW 코사인) |
| 캐시/실시간 | Redis 7 / SSE |
| AI | Spring AI 1.1 + Gemini 2.0 Flash(채팅) · text-embedding-004(임베딩), AI Studio 무료 티어 |
| 수집 | The Guardian Content API |
| 마이그레이션 | Flyway |
| 문서 | springdoc-openapi (Swagger UI) |

## 패키지 구조 (feature 기반)

```
com.newslens
├── common      공통 응답(ApiResponse)·예외(GlobalExceptionHandler)·이벤트
├── config      OpenAPI 등 설정  (SecurityConfig 자리 — 스트레치)
├── article     Feed/Article/Category 엔티티·API   (읽기)
├── collect     Guardian 클라이언트·스케줄러·중복제거·LLM 처리·이벤트 발행  (쓰기)
├── search      RAG 검색  (읽기)
├── realtime    SSE 구독·Emitter 레지스트리·이벤트 리스너  (읽기)
└── auth        로그인·JWT 필터  (스트레치)
```

## 로컬 실행

```bash
# 1) 인프라 기동 (Postgres + Redis)
docker compose up -d

# 2) 환경변수 — .env.example 복사 후 키 채우기
cp .env.example .env   # GEMINI_API_KEY, GUARDIAN_API_KEY 입력

# 3) 앱 실행 (.env 의 값을 환경변수로 주입해서)
./gradlew bootRun

# 헬스체크: http://localhost:8080/actuator/health
# API 문서:  http://localhost:8080/swagger-ui.html
```

> Gemini/Guardian 키 없이도 앱은 부팅된다(헬스 UP). 실제 수집·요약·RAG 호출 시 키가 필요하다.

## 데이터 흐름

```
Guardian API → 스케줄러 → 중복제거 → LLM(요약·태깅 1콜 + 임베딩 1콜) → PostgreSQL+pgvector
                                                                          │
                                                  NewArticlesEvent ───────┘
                                                       ├─ SSE 푸시(new-articles)
                                                       └─ Redis 캐시 무효화

클라이언트 → REST API(Feed/Article/Category, RAG 검색 /api/search, SSE /api/subscribe)
```

기획·설계 상세: TIL `toyProject/newslens` 문서(개요·병합계획·업무가이드).
