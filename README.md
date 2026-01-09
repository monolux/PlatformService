### 프로젝트 개요 ###
- 프로젝트명 : Platform Service
- 목적 : 회원 관리 및 인증 기능을 중심으로 한 예제 프로젝트
- 아키텍쳐 : Oauth 2.0 기반 인증, 인가 적용
- 빌드 도구 : Gradle + Groovy
- JDK : Java 17

### 구동 방법 ###
- Docker 설치
- MSSQL 컨테이너 구동
  + cmd -> ./Docker/Compose/MSSQL 경로로 이동 후 docker-compose up -d
- Redis 서버 구동 : ./Docker/Compose/Redis/docker-compose.yml
  + cmd -> ./Docker/Compose/Redis 경로로 이동 후 docker-compose up -d

### 모듈 설명 ###
1. authorization
   + Scale out 을 고려한 Session Clustering.<br>여러대의 인증 서버를 구동, 로드밸런싱을 할 경우 /oauth/authorize 엔드포인트 호출시 전달되는 파라미터가 (client_id, redirect_uri, response_type 등) 공유되지 않음. 
   + Access Token, Authentication 객체를 Redis 로 캐싱, DB I/O 최적화.
   + TOTP + QR 을 통해 MFA 적용, OTP App 은 Google or MS Authenticator 사용
   + 2차 인증 설정을 위한 QR or OTP Secret 제공
   + Bot 탐지를 위해 CloudFlare Turnstile 적용
   + 서버 상태 모니터링을 위해 Spring Boot Actuator 및 Prometheus 엔드포인트를 구성
   
2. resource : 리소스 서버
   + 본 프로젝트의 주 목적이 인증에 대한 학습이었기에 Reource Server 기능은 최소화, 회원 가입 관련 API 만 존재
   + 서버 상태 모니터링을 위해 Spring Boot Actuator 및 Prometheus 엔드포인트를 구성
   + API 문서화를 위해 Swagger(OpenAPI)를 적용
     + secured API 와 public API 의 definition 을 분리

3. Domain Module : 도메인 로직을 작성한 모귤
   + Spring Data JPA 및 QueryDSL을 활용한 ORM 기반 데이터 접근 계층 구성

4. Encryption : AES, DES 등등의 암호화 처리를 하는 모듈
   + CBC, CFB, CTR, ECB, GCM, OFB 모드 지원
   + 최소한이긴 하지만 암호화 모듈에 대한 테스트 코드는 필요하다 판단되어 Test 코드 작성

5. Util : 여러 모듈에서 참조하는 Util Class

6. Common : 여러 프로젝트에서 참조하는 공통 Class 또는 상수

### 개선 포인트 ###
- 조직 내에서 사용하는 라이브러리 및 빌드 산출물을 중앙에서 관리·배포하기 위해 Nexus Repository 도입 필요.
- 서비스 환경 설정을 중앙에서 관리하고, 환경별 설정 분리와 변경 이력을 체계적으로 관리하기 위해 Spring Cloud Config 도입 필요.