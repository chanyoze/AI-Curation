-- NewsLens 초기 스키마 (업무 가이드 2-1 계약)
-- 벡터 차원 768 = text-embedding-004 기본. 인덱스는 HNSW + 코사인.
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE articles (
  id           BIGSERIAL PRIMARY KEY,
  source       VARCHAR(30)  NOT NULL,          -- 'guardian'
  source_id    VARCHAR(200) NOT NULL,          -- Guardian 기사 id (중복 판별 키)
  url          TEXT         NOT NULL,
  title_en     TEXT         NOT NULL,          -- 원문 제목
  title_ko     TEXT,                           -- 번역 제목
  summary_ko   TEXT,                           -- 한국어 3줄 요약
  content_en   TEXT,                           -- 원문 본문(전문)
  category     VARCHAR(40),                    -- LLM 태깅 (예: AI/ML, 인프라)
  published_at TIMESTAMPTZ,
  created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
  embedding    vector(768),                    -- 한국어 요약 임베딩
  CONSTRAINT uq_articles_source UNIQUE (source, source_id)
);
CREATE INDEX idx_articles_published ON articles (published_at DESC);
CREATE INDEX idx_articles_category  ON articles (category);
CREATE INDEX idx_articles_embedding ON articles USING hnsw (embedding vector_cosine_ops);

CREATE TABLE article_tags (
  article_id BIGINT      NOT NULL REFERENCES articles(id) ON DELETE CASCADE,
  tag        VARCHAR(40) NOT NULL,
  PRIMARY KEY (article_id, tag)
);

-- [~ 스트레치] JWT 자리 예약 — 8일 코어 미사용
CREATE TABLE users (
  id            BIGSERIAL PRIMARY KEY,
  email         VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);
