# 웹스토어
포인트를 사용해 아이템을 구매하고 적립할 수 있는 웹스토어 서비스입니다.

## 사용기술
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
</div>

## ERD
![erd](https://github.com/Cathunder/WebStore/assets/102372626/2e25f6a0-08ca-4600-9e92-bf9e15d67a3f)


## 프로젝트 기능 및 설계
### [유저]
- 회원가입
  - 이메일, 비밀번호, 닉네임
  - 가입 시 일정 포인트 제공
  - 구글 OAuth2 사용
- 로그인
  - 이메일, 비밀번호, JWT
  - 구글 OAuth2 사용
- 포인트 내역 조회
  - 구매 아이템명, 포인트 증감 수치, 적립/사용 여부, 적립/사용 날짜 

### [포인트 적립]
- 출석 버튼 클릭 시 하루 한번 랜덤한 양의 포인트 적립

### [아이템 구매]
- 판매 아이템 조회
  - 조회시 캐싱 사용
- 포인트를 지불하여 아이템 구매 가능
  - 로그인된 유저만 구매 가능
  - 아이템 구매 즉시 포인트 또는 캐시가 적립됨
- 박스 구매 시 재고에 따른 동시성 이슈 처리
  - redis 사용

### [아이템]
- 포인트 박스
  - 랜덤형
    - 1 ~ 100 사이의 포인트 적립
  - 고정형
    - 관리자가 설정한 확률에 따라 정해진 포인트 적립 
    - 포인트: 100, 200, 500 
    - 확률: 70%, 20%, 10%
  - 모든 포인트 박스들은 수량 및 일일 구매 횟수에 제한이 있음
  - 모든 포인트 박스들은 매일 24시에 수량이 초기화
    - 스케쥴러 사용
  - 일부 포인트 박스들은 정해진 기간에만 구매가 가능
- 캐시
  - 포인트와 1:1 비율로 구매가 가능
  - 100 포인트 -> 100 캐시

### [관리자]
- 아이템 등록, 조회, 수정, 삭제
  - 아이템명, 아이템 수량, 구매에 필요한 포인트, 적립되는 포인트 등
- 유저의 포인트 내역 조회
  - 유저 이메일, 구매 아이템명, 포인트 증감 수치, 적립/사용 여부, 적립/사용 날짜
  - 캐싱기능 사용
- 핫딜, 이벤트 등의 알림 전송 
  - 특정 시간에 해당 알림을 전송할 수 있는 기능
  - WebSocket 사용

### [채팅]
- 유저와 관리자간 1:1 문의 채팅
- WebSocket 사용
