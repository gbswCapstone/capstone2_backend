<div align="center">

<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EC%9A%B0%EA%B1%B0%EC%9A%B0%EA%B1%B0_%EC%B1%97%EB%B4%87_icon.png" width="80">

# 우거우거

### 팩트 체크로 시작하는 소비 습관 관리 앱

<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EC%9A%B0%EA%B1%B0%EC%9A%B0%EA%B1%B0_%EB%AA%A9%EC%97%85.png" width="800">

<br>

<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/springBoot_icon.png" width="40" height="40" />
<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/mysql_icon.png" width="40" height="40" />
<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/aws_icon.png" width="40" height="40" />
<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/AWS_EC2_icon.png" width="40" height="40" />
<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/flutter-icon.png" width="40" height="40" />
<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/webSocket_icon.png" width="40" height="40" />
<img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/github_icon.png" width="40" height="40" />

</div>

---

## ▶️ 시연 영상

<div align="center">
  <a href="https://www.youtube.com/watch?v=vRDuY57DHCQ">
    <img src="https://img.youtube.com/vi/vRDuY57DHCQ/maxresdefault.jpg" alt="시연 영상" width="800">
  </a>
</div>

---

## 📊 최종 발표 자료

> 🎨 [Canva 최종 발표 보러가기](https://canva.link/1tt7qzekx5vev4y)

---

## 프로젝트 소개

AI 기반 소비 분석과 게임화 요소를 결합해 사용자가 지속적으로 건강한 소비 습관을 만들어갈 수 있도록 돕는 서비스입니다.

영수증 촬영으로 소비 내역을 자동 추출하고, AI가 팩트 기반 피드백으로 소비 패턴을 직접 진단합니다.
단순한 기록을 넘어 챌린지·랭킹·캐릭터 시스템으로 절약을 지속할 수 있는 동기를 제공합니다.

---

## <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/solid_logo.png" width="20"> 팀 · solid

> 흔들림 없이 단단한 기술과 가치를 만들어가는 팀  
> 구성 : 프론트엔드 1 · 백엔드 1 · AI 1 · 디자인 3

| 이름 | 역할 | 담당 |
|------|------|------|
| 변지우 | 팀장 | Flutter 프론트엔드 |
| 최윤정 | 팀원 | Spring Boot 백엔드 · 디자인 |
| 이시은 | 팀원 | Python AI (OCR · 소비 분석) |
| 김새은 | 팀원 | UI/UX 디자인 (Figma) |
| 이태인 | 팀원 | UI/UX 디자인 (Figma) |

---

## ☁️ 시스템 아키텍처

<div align="center">
  <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EC%9A%B0%EA%B1%B0%EC%9A%B0%EA%B1%B0_%EC%8B%9C%EC%8A%A4%ED%85%9C%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.png" alt="서비스 아키텍처" width="800">
</div>

---

## 🛠 기술 스택

<div align="center">
  <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EC%9A%B0%EA%B1%B0%EC%9A%B0%EA%B1%B0_%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC.png" alt="프레임워크" width="800">
</div>

<br>

| 구분 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/springBoot_icon.png" width="16"> Spring Boot 3.5.6 |
| Database | <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/mysql_icon.png" width="16"> MySQL 8.0, Spring Data JPA, QueryDSL 5.0 |
| Security | Spring Security, OAuth2 (Google/Kakao), JWT |
| Real-time | <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/webSocket_icon.png" width="16"> WebSocket + STOMP |
| Storage | Firebase Storage |
| HTTP Client | Spring Cloud OpenFeign |
| Frontend | <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/flutter-icon.png" width="16"> Flutter |
| AI | Python |
| Infra | <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/aws_icon.png" width="16"> AWS EC2, Docker, Nginx |
| API Docs | Swagger (Springdoc OpenAPI 2.x) |
| Collaboration | <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/stacks/github_icon.png" width="16"> Github, Notion |
| Build | Gradle 8.9 |

---

## 🗄 ERD

<div align="center">
  <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EC%9A%B0%EA%B1%B0%EC%9A%B0%EA%B1%B0_ERD.png" alt="ERD" width="800">
</div>

---

## ✨ 주요 기능

### 🧾 영수증 OCR · 소비 내역 자동 추출
- 영수증 촬영 → AI OCR로 소비 항목·금액 자동 추출 및 저장
- 카테고리 자동 분류로 수동 입력 없이 가계부 관리 시작
- Firebase Storage 연동 이미지 처리

### <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EC%9A%B0%EA%B1%B0%EC%9A%B0%EA%B1%B0_%EC%B1%97%EB%B4%872.png" width="20"> AI 팩폭 · 소비 분석
- 누적 소비 데이터 기반 팩트 피드백 제공
- 카테고리별 과소비 패턴 자동 감지
- 소비 MBTI 분석 (한탕주의형, 찔끔형 등)
- 가상 파산 시뮬레이션 — 현재 소비 습관 유지 시 미래 재산 예측
- WebSocket 기반 실시간 1:1 AI 소비 상담 챗봇

### 💬 실시간 채팅
- WebSocket · STOMP 기반 실시간 양방향 통신
- 챌린지 참여 유저 간 단체 채팅방
- 채팅 메시지 송수신 및 입장/퇴장 이벤트 처리
- ChannelInterceptor 기반 Handler 책임 분리로 채팅 간 충돌 없는 안정적인 서비스 구현

### 🏆 챌린지 · 랭킹
- 챌린지 생성/참여/탈퇴, 해시태그
- 절약왕 / 탕진왕 등급제
- 일일 미션 자동 생성 및 달성 여부 자동 판별 (Spring Scheduler)
- 미션 성공 시 경험치 적립 및 레벨업 보상 자동 처리

### <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/%EB%9E%AD%ED%82%B9_icon.png" width="20"> 캐릭터 성장 시스템
- 잔액·등급·소비 내역에 따라 캐릭터 외형 변화
- 경험치 기반 레벨·랭킹 시스템

### 📊 소비 분석 · 가계부
- 수입/지출 카테고리별 통계
- 기간/날짜 필터링 및 정렬
- 지출 내역 공유 커뮤니티

### 🔐 인증 · 보안
- Google · Kakao OAuth2 소셜 로그인
- JWT Access Token + Refresh Token Rotation
- HttpOnly 쿠키, CORS, XSS 방지

---

## 🚀 기술적 성과

### QueryDSL 동적 쿼리 최적화
불필요한 쿼리 제거로 조회 속도 **202ms → 120ms (약 41% 향상)**

### Firebase Storage 연동 이미지 처리 개선
영수증 업로드 로직 개선으로 이미지 처리 응답속도 **약 29% 단축**

### Handler 책임 분리
ChannelInterceptor 구조를 활용해 채팅 간 충돌 없는 안정적인 실시간 서비스 구현

### 미션 자동화
Spring Scheduler 기반 일일·무지출·월 목표 미션 달성 여부 자동 판별 및 보상 처리

---

## API 엔드포인트

- **Swagger UI:** `/swagger-ui.html`
- **API Prefix:** `/api/*`

| 카테고리 | 주요 엔드포인트 |
|----------|----------------|
| Auth | 회원가입, 로그인, 토큰 재발급, OAuth2 (Google/Kakao) |
| User | 프로필 조회/수정, 캐릭터, 잔액 |
| Challenge | 생성/조회/참여, 채팅방, 미션, 공지 |
| Board | 게시글 CRUD, 이미지/영상, 댓글, 좋아요 |
| Mission | 미션 목록, 완료 처리, 진행 상황 |
| Ranking | 사용자 랭킹 조회 |
| Usage | 수입/지출 기록, OCR 저장, 통계, 필터링 |
| ChatBot | AI 소비 상담 챗봇 (WebSocket) |

---

## 🚀 Getting Started

```bash
# 환경 변수 설정
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties

# 빌드
./gradlew clean build

# 로컬 실행
./gradlew bootRun

# Docker 빌드 & 실행
docker build -t woogeo-backend .
docker run -p 8080:8080 woogeo-backend
```

---

## 📁 프로젝트 구조

```
src/main/java/.../capstoneProject/
├── controller/     # REST API + WebSocket 컨트롤러 (13개 도메인)
│   ├── auth/       # 인증 (회원가입, 로그인)
│   ├── challenge/  # 챌린지, 채팅방, 미션, 공지
│   ├── board/      # 게시판
│   ├── chatbot/    # AI 소비 상담 챗봇
│   └── usage/      # 가계부 (OCR 포함)
├── service/        # 비즈니스 로직 (12개 도메인)
├── repository/     # 데이터 접근 계층 (JPA, QueryDSL, 36개)
├── entity/         # JPA 엔티티 + enums (24개)
├── dto/            # 요청/응답 DTO
├── security/       # JWT, OAuth2, Spring Security
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomOauth2UserService.java
├── config/         # 설정 클래스 (12개)
├── global/         # ApiResponse, GlobalResponseHandler
├── exceptions/     # 도메인별 커스텀 예외
├── event/          # 출석·미션 완료 이벤트
├── scheduler/      # 일일 미션 스케줄러
└── policy/         # 레벨/경험치 정책
```

---

<div align="center">
  <img src="https://raw.githubusercontent.com/yoonjeonggg/readme-assets/main/woogeo/solid_logo.png" width="60">
  <br>
  <sub>팀 solid - 2025 2학기 캡스톤 프로젝트</sub>
</div>
