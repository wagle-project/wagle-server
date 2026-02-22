# 🎪 WagleWagle Server (와글와글 백엔드)

> **Real-time Festival Congestion Monitoring Service**<br>
> 야외 축제장 실시간 혼잡도 및 위치 확인 서비스 '와글와글'의 백엔드 리포지토리입니다.

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=spring-boot)
![Redis](https://img.shields.io/badge/Redis-Geo-DC382D?style=flat-square&logo=redis)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql)

## 📖 Project Overview
'와글와글'은 넓은 야외 축제장에서 발생하는 **"인파 혼잡"**을 해결하기 위한 위치 기반 서비스입니다.<br>
기존의 종이 팜플렛을 디지털화하고 실시간으로 사용자의 위치를 집계하여 **직관적인 혼잡도(Heatmap/Hexagon)** 정보를 제공합니다.

## 🚀 Key Technical Decisions
> 해당 프로젝트에서 저희는 대용량 트래픽 처리를 위해 다음과 같은 기술적 도전을 했습니다.

### 1. Redis Geo 기반 실시간 위치 추적 (Location Tracking)
- **Challenge**: 수천 명의 사용자가 1초마다 위치를 갱신할 경우, RDB의 I/O 부하가 감당할 수 없을 만큼 증가합니다.
- **Solution**:
    - 위치 정보는 영구 저장이 필요 없는 휘발성 데이터입니다.
    - **Redis Geo 자료구조**를 사용하여 **In-Memory**에서 위치 갱신 및 조회 성능을 `O(1)`~`O(logN)`으로 최적화했습니다.
    - `Expire` 기능을 활용해 1분 이상 응답이 없는(Heartbeat miss) 사용자를 자동으로 집계에서 제외합니다.

### 2. Uber H3 육각형 클러스터링 (Spatial Aggregation)
- **Challenge**: 접속자 전원의 좌표(Point)를 프론트엔드로 전송할 경우 렌더링 부하로 인해 모바일 기기가 멈추는 현상이 발생합니다.
- **Solution**:
    - **Uber H3 Spatial Index** 알고리즘을 도입하여 지도를 육각형(Hexagon) 그리드로 나눕니다.
    - 서버에서 미리 구역별 인원수를 집계(Aggregation)한 뒤 **`HexID`와 `밀집도 Level`** 만 전송하여 데이터 전송량을 90% 이상 절감했습니다.

## 🛠 Tech Stack

| Category | Technology        | Description            |
| :--- |:------------------|:-----------------------|
| **Framework** | Spring Boot 3.4.2 | Java 21 기반의 견고한 서버 구축  |
| **Database** | MySQL 8.0         | 사용자, 축제 정보 등 정형 데이터 관리 |
| **Cache/NoSQL** | Redis             | 실시간 위치 저장(Geo) 관리      |
| **ORM** | Spring Data JPA   | 객체 지향적인 데이터 접근 계층 구현   |
| **Auth** | JWT               | Stateless한 인증 처리       |
| **Library** | Uber H3 (Java)    | 육각형 공간 인덱싱 알고리즘        |

## 📂 Architecture Structure
도메인형 디렉토리 구조를 채택하여 비즈니스 복잡도를 관리합니다.
```text
com.waglewagle.server
├── global          # 전역 설정 (Config, Error, Util)
├── domain
│   ├── festival    # 축제 정보 관리
│   ├── member      # 사용자 인증/인가
│   ├── map         # 지도 오버레이 및 타임테이블
│   └── location    # [Core] 실시간 위치 및 H3 집계 로직
└── WagleServerApplication.java

```

## 🔧 Getting Started (Local)

1. **Clone Repository**
```bash
git clone [https://github.com/wagle-wagle/wagle-server.git](https://github.com/wagle-wagle/wagle-server.git)

```

2. **Setup Environment**
* `application.yml` 파일 생성 및 DB 설정 (MySQL, Redis 포트 확인)

3. **Run Application**
```bash
./gradlew bootRun

```

## 📝 Documentation

* [📄 기술 스택 결정안 ](./docs/convention.md)
* [🎨 ERD 설계도 ](./docs/erd.md)

