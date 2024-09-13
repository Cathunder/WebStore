# 웹스토어
포인트를 사용해 포인트 상자를 구매하거나 캐시로 교환할 수 있는 웹 스토어입니다.

## 사용기술
<div> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  </br>
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  </br>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
</div>

## ERD
![erd](https://github.com/user-attachments/assets/c20a96fe-9e01-474d-8c2a-d6d49813f1ed)

## 프로젝트 기능 및 설계
### [유저]
- 회원가입
  - 이메일, 비밀번호, 닉네임
  - 가입 시 일정 포인트 제공
- 로그인
  - 이메일, 비밀번호, JWT
- 포인트 내역 조회
  - 구매 아이템명, 포인트 증감 수치, 적립/사용 여부, 적립/사용 날짜
- 캐시 내역 조회
  - 적립량, 적립날짜

### [포인트 적립]
- 출석 버튼 클릭 시 하루 한번 랜덤한 양의 포인트 적립

### [아이템 구매]
- 판매 아이템 조회
- 포인트를 지불하여 아이템 구매 가능
  - 로그인된 유저만 구매 가능
  - 아이템 구매 즉시 포인트 또는 캐시가 적립됨
- 박스 구매 시 재고에 따른 동시성 이슈 처리
  - redis 사용

### [아이템]
- 포인트 박스 아이템
  - 포인트 박스의 종류는 2가지로 랜덤형과 고정형이 존재
    - 랜덤형
      - 랜덤형 포인트 박스는 (최소포인트, 최대포인트)를 가짐
      - 구매시 랜덤한 확률로 범위값 내 포인트중 하나가 지급됨
      - X 랜덤 포인트 박스: (1, 10000) -> 1 ~ 10000 사이의 포인트 지급
      - Y 랜덤 포인트 박스: (500, 600) -> 500 ~ 600 사이의 포인트 지급
    - 고정형
      - 고정형 포인트 박스는 여러개의 (포인트, 확률)을 가짐
      - 구매시 확률에 따라 선택된 포인트가 지급됨
      - A 고정 포인트 박스: (100, 70%), (200, 20%), (500, 10%) -> 20% 확률로 200 포인트 지급
      - B 고정 포인트 박스: (100, 95%), (1000, 5%) -> 95% 확률로 100 포인트 지급
  - 모든 포인트 박스들은 수량 및 일일 구매 횟수에 제한이 있음
  - 모든 포인트 박스들은 매일 24시에 수량이 초기화
    - 스케쥴러 사용
  - 일부 포인트 박스들은 정해진 기간에만 구매가 가능
- 캐시 아이템
  - 포인트와 1:1 비율로 구매가 가능
  - 100 포인트 -> 100 캐시
  - 모든 캐시 아이템들은 일일 구매 횟수에 제한이 있음

### [관리자]
- 아이템 등록, 조회, 수정, 삭제
  - 아이템명, 아이템 수량, 구매에 필요한 포인트, 적립되는 포인트 등
- 유저의 포인트 내역 조회
  - 유저 이메일, 구매 아이템명, 포인트 증감 수치, 적립/사용 여부, 적립/사용 날짜
- 유저의 캐시 내역 조회

## 추가로 구현할 기능
- 소셜 로그인 기능 추가(구글, 카카오, 네이버)
- 조회시 캐싱 기능 추가
- SSE를 사용한 실시간 알림 전송 기능 추가(ex. 이벤트 하루전 알림 전송)
- WebSocket을 사용한 유저, 관리자간 1:1 문의 채팅 기능 추가