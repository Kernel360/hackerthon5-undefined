# Uptime

## 개요
이 프로젝트는 사용자의 웹 사이트 사용시간을 모니터링하는 서비스를 개발하는 프로젝트입니다.

## 기술 스택
### Backend
* Java 21
* Spring Boot 3.4.5
* Gradle 8.13
* Spring Data JPA
* nginx


### Frontend

### Infra
* terraform
* AWS EC2
* Amazon RDS for MySQL
* Amazon ECR

### CI/CD
* GitHub Actions
* Docker

## 주요 기능
* Agent
  * 크롭 익스텐션으로 개발
  * 사용자의 웹 페이지 활성화 상태를 모니터링 및 메트릭 수집, 전송
* Server
  * 메트릭 수집, 분석 및 저장
  * 메트릭 데이터 재가공
* UI
  * 사용자의 웹 페이지 활성화 상태를 시각화하여 제공
  * 사용자 도메인별 사용시간 분석


## API
### Metric 기능 API
| Method | URL                      | 설명 |
|--------|--------------------------|----|
| GET    | /api/metrics             | 기간 단위 사용량 조회 요청 API |
| POST   | /api/metrics             | 추적 데이터를 추가 |
| GET | /api/metrics/daliy-usage | 데일리 사용량 조회 요청 API |
| GET| /api/metrics/daliy-usage/trace | 24시간 동안 사용한 도메인 집계 API |
| GET | /api/metrics/daliy-usage/domain-usage-ratio | 통계 정보 요청 API |

### Member 기능 API
| Method | URL                      | 설명         |
|--------|--------------------------|------------|
| PUT | /api/v1/member/setProfile | 프로필 등록 API |
| GET | /api/v1/member/login/oauth/{provider} | OAuth2 로그인 API |
| GET | /api/v1/member/getProfile | 프로필 조회 API |