# 📌 Project Ground Rules & Tech Stack

이 문서는 프로젝트 협업 규칙, 아키텍처 원칙 및 구현 세부 사항을 정의하여 팀의 일관성을 유지하는 것을 목적으로 합니다.

## 1. 🤝 Collaboration Rules

### Development Environment
* **OS:** Windows / macOS
* **IDE:** IntelliJ IDEA (UTF-8 설정 필수)
* **JDK:** OpenJDK 21
* **Docker:** 로컬 DB 환경 구축용

### Git Strategy
* **Branch Strategy:** `CONTRIBUTING.md`의 컨벤션 준수
* **Commit Message:** `CONTRIBUTING.md`의 컨벤션 준수
* **Code Review:**
    * PR 생성 시 관련 Issue 링크 필수 (`Closes #12`)
    * **팀원 1명 이상의 Approve** 후 Merge 가능

## 2. 📐 Design & Documentation

| 구분 | 도구 (Tool) | 관리 정책 |
| :--- | :--- | :--- |
| **API 명세서** | **Notion** | API Endpoint, Request/Response, Status Code 명시 (백엔드 내부 소통용) |
| **에러 코드** | **Notion** | 전역적으로 사용되는 커스텀 에러 코드 및 메시지 관리 |
| **ERD** | **Mermaid** | `docs/erd/*.md` 파일로 관리하여 Git에 포함 |
| **인프라 아키텍처** | **Draw.io** | `docs/infra/architecture.png` 등의 이미지 파일로 저장 |

## 3. 🛠 Implementation Details

### Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3.4.2
* **Build Tool:** Gradle
* **Cloud Platform:** Naver Cloud Platform (NCP)
* **Database:**
    * **Local:** Docker MySQL 8.0 (공간 데이터 함수 호환성을 위해 로컬 환경 내에서도 MySQL을 사용)
    * **Prod:** MySQL 8.0.43
    * **Cache:** Redis
* **Key Libraries:**
    * **Uber H3:** 육각형 그리드 기반 혼잡도 계산 및 인덱싱
    * **Hibernate Spatial:** MySQL 공간 데이터(Point, Polygon) 처리

### Architecture & Pattern
* **Project Structure:** 도메인형 패키지 구조 (Domain-driven Directory Structure)
* **Data Access:**
    * Spring Data JPA (기본 CRUD)
    * JPQL 쿼리 사용
* **API Documentation:** Swagger (Springdoc OpenAPI)
    * *Policy:* `Docs` 컨트롤러 등을 활용하여 프로덕션 코드와 문서화 코드를 분리하는 방식 지향

### Features & Logic
* **Authentication (인증/인가):**
    * **Strategy:** JWT (Access Token) + UUID 기반 익명 인증 (Anonymous Auth)
    * **Mechanism:** 사용자 회원가입 없이 기기/세션 기반의 '투명 로그인(Silent Login)' 구현
* **DTO Strategy:**
    * Java **`record`** 사용 (불변성 보장)
    * **Inner Record Pattern:** `MemberDto.java` 내부에 `JoinRequest`, `InfoResponse` 등을 `static record`로 그룹화
    * **Mapper:** DTO 내부의 `toEntity()`, `from()` 메서드를 통해 변환 로직 캡슐화
* **Data Management:**
    * **Delete Policy:** Hard Delete를 기본으로 하되 복구가 필요한 중요 데이터는 Soft Delete (`deleted_at`) 적용
    * **Paging:** 클라이언트 요청 없을 시 기본 `size=10` 적용 (0-based index)
* **Global Handling:**
    * **Response:** `ApiResponse<T>` 형태의 통일된 응답 포맷 사용
    * **Exception:** `@RestControllerAdvice`를 활용한 전역 예외 처리

### DevOps
* **CI/CD:** GitHub Actions (CI) + Docker (Containerization) + NCP (Deployment)
* **Infrastructure R&R:**
    * **Docker 담당:** `Dockerfile` 최적화, `docker-compose` (로컬/운영) 관리, Github Actions 파이프라인 구축
    * **Redis/DB 담당:** Redis 캐싱 전략(TTL 등), MySQL 스키마/인덱스 관리, 데이터 백업 전략