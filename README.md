### 프로젝트 개요 ###
- 프로젝트명 : Platform Service
- 목적 : 회원 관리 및 인증 기능을 중심으로 한 예제 프로젝트
- 아키텍쳐 : OAuth 2.0 기반 인증 및 인가 시스템
- 빌드 도구 : Gradle (Groovy DSL)
- JDK 버전 : Java 17

### 주요 구동 환경 ###
- 인프라 : Docker 및 Docker Compose
- 데이터베이스 : MSSQL (Microsoft SQL Server)
- 캐시 서버 : Redis (Session Clustering 및 Token Caching용)

### 프로젝트 모듈 구성 ###
```text
com.monolux
├── authorization       : OAuth 인증 서버 모듈
│   ├── configs         : 인증 및 보안 설정
│   ├── controllers     : 인증 관련 API
│   │   ├── opened      : public
│   │   └── secured     : private
│   ├── event           : Oauth 인증 성공 및 실패에 대한 패키지
│   │   ├── handlers    : 핸들러
│   │   └── publisher   : 퍼블리셔
│   ├── filters         : 인증 시큐리티 필터
│   └── oauth2          : Oauth 인증 인터페이스 구현체
├── common              : 프로젝트 전반에 공통으로 사용되는 모듈
│   ├── constants       : 상수
│   │   ├── http        : http 관련 상수값
│   │   └── oauth2      : 인증관련 상수값
│   └── web             : Web 관련 공통 클래스
│       └── response    : API 결과값에 대한 추상화
├── domain              : 도메인 로직에 대한 모듈
│   ├── auditors        : Entity Auditor 관련 클래스
│   ├── configs         : JPA 관련 설정
│   ├── entities        : 도메인 엔티티
│   │   ├── embeddables : 엔티티에 반복적으로 나타나는 컬럼들을 의미 단위로 묶은 클래스
│   │   │   └── id
│   │   └── listeners   : 엔티티 Listener, 엔티티들의 상태 변화를 감지
│   ├── enumerations    : 엔티티들의 열거 타입
│   ├── exceptions      : 예외처리를 위한 예외 클래스
│   ├── repositories    : Entity Repository
│   └── services        : Domain Service
├── encryption          : 암호화 모듈
│   └── block           : Block 암호화 모듈
├── resource            : 도메인 로직이 구현된 resource server
│   ├── configs         : 서버 설정 관련 클래스
│   ├── controllers     : 도메인 로직 관련 API
│   │   ├── opened      : public
│   │   └── secured     : private
│   ├── dto             : API 전송 객체
│   │   ├── req
│   │   └── res
│   └── handlers        : API의 예외를 RestControllerAdvice 를 통해 일관성 있게 관리
└── utils               : 프로젝트 전반에서 사용되는 Util 클래스
    └── converters      : 데이터 타입 변환
```
1. **authorization (인증 서버)**
    - OAuth 2.0 및 MFA(TOTP) 적용
    - Redis 기반 세션 클러스터링 및 토큰 캐싱
    - CloudFlare Turnstile을 이용한 봇 방지
    - Actuator 및 Prometheus 모니터링 지원
    - **상세 패키지**:
        - `configs`: 인증 및 보안 설정
        - `controllers`: 인증 관련 API (`opened`, `secured` 분리)
        - `event`: 이벤트 관리 (`handlers`, `publisher`)
        - `filters`: 보안 및 공통 필터
        - `oauth2`: OAuth 2.0 커스텀 로직

2. **resource (리소스 서버)**
    - 회원 가입 및 기본 API 제공
    - Swagger(OpenAPI)를 활용한 API 문서 자동화
    - Actuator 및 Prometheus 모니터링 지원
    - **상세 패키지**:
        - `configs`: 리소스 서버 보안 및 환경 설정
        - `controllers`: 리소스 관련 API (`opened`, `secured` 분리)
        - `dto`: 요청(`req`) 및 응답(`res`) 데이터 전송 객체
        - `handlers`: 예외 처리 및 공통 핸들러

3. **domain (도메인 모듈)**
    - JPA 및 QueryDSL 기반의 데이터 접근 계층
    - **상세 패키지**:
        - `auditors`: 데이터 변경 이력 감사 설정
        - `configs`: 도메인 레이어 설정 (JPA, QueryDSL 등)
        - `entities`: JPA 엔티티 정의 (`embeddables`, `listeners` 포함)
        - `enumerations`: 공통 열거형 정의
        - `exceptions`: 도메인 특정 예외 클래스
        - `repositories`: 데이터 저장소 인터페이스
        - `services`: 도메인 비즈니스 로직

4. **encryption (암호화 모듈)**
    - 다양한 암호화 알고리즘(AES, DES 등) 및 모드(CBC, GCM 등) 지원
    - 단위 테스트를 통한 암호화 로직 검증
    - **적용 디자인 패턴**:
        - **Strategy 패턴**: `Encryptor` 인터페이스를 통해 다양한 암호화 알고리즘 및 운영 모드별 로직을 추상화하고 런타임에 교체 가능하도록 설계했습니다.
        - **Factory 패턴**: `EncryptorFactory`를 통해 알고리즘 설정에 따른 구체적인 `Encryptor` 인스턴스 생성을 캡슐화했습니다.
    - **상세 패키지**:
        - `block`: 블록 암호화 알고리즘 구현체 (AES, DES 등)

5. **utils (유틸리티 모듈)**
    - 공통 유틸리티 클래스 모음
    - **상세 패키지**:
        - `converters`: 데이터 타입 변환 유틸리티

6. **common (공통 모듈)**
    - 전역 상수 및 공통 클래스 관리
    - **상세 패키지**:
        - `constants`: 공통 상수 (`http`, `oauth2` 분류)
        - `web`: 웹 관련 공통 응답(`response`) 처리

### 개선 과제 ###
- Nexus Repository 도입을 통한 빌드 산출물 관리
- Spring Cloud Config 기반의 중앙 집중식 설정 관리
