# 지자체 협약 지원 정보 보기
 
- 구현 환경  
Spring boot    
Java 1.8  
Gradle  
undertow  
H2 Database  

--- 

- 분석 데이터  
파일내용: 지자체 협약 지원 정보 보기  
파일명 : local_government_support_info.csv 
파일위치 : jar 빌드 파일(local-0.0.1-SNAPSHOT.jar)와 같은 위치에 csv 파일을 저장 해놓으면 분석이 가능하다.   

---

- 단위 테스트  
1. 기본 기능 
-> csv 파일 읽기  
-> 지자체명 및 코드 DB 저장  
-> 지자체협약지원정보 DB 저장  

2. 지자체 협약 지원 정보 테스트  
-> 지자체 지원정보 전체 출력  
-> 특정 지자체 지원정보 검색  
-> 지자체 지원정보 수정  
-> 지자체 지원정보 내림차순 조회  
-> 지자체 이차보전 비율 최소 조회  

3. 지자체 정보 테스트  
-> 지자체 코드 조회  
-> 지자체 리스트 조회  

4. 지자체 정보 추천 테스트  
-> 지자체 지원정보 추천  

5. Security 테스트  
-> 가입 테스트   
-> 로그인 테스트  
-> 토큰 갱신 테스트  

---  

- 빌드 방법  
1. gradle 설치  
2. 프로젝트 루트 접근  
3. `gradle build` 명령어 실행  
4. 프로젝트 루트의 `build\libs` 위치에 jar 파일 생성 확인  

---  

- 구동 방법  
-> cmd나 쉘에서 아래 명령어를 입력  
-> `java -jar -Dspring.profiles.active=local local-0.0.1-SNAPSHOT.jar`(jar 파일 경로)  

---  

- 실행 가능한 릴리즈 파일  
-> https://github.com/sung9020/Housing-finance-API/releases  

---  

- API 명세를 보기위한 Swagger 주소  
-> localhost:9090/swagger-ui.html   

---  

- Swagger 테스트 시에 주의점  
-> 회원가입, 로그인 API 외에는 토큰 인증을 거쳐야 API 호출이 가능하다.  
-> 아래 스크린샷과 같이 Authorize 버튼을 누른 후에 Bearer <토큰> 을 넣어주면 API 호출이 가능해진다.  

---  

- API 요청 프로세스  
-> 회원 가입 및 로그인을 하지 않으면 구현된 API를 호출 할 수 없다.  
-> 회원 가입을 위해 `localhost:9090/api/auth/signUp`을 호출하면 토큰이 발급된다.  
-> 이미 회원 가입을 하였으면, `localhost:9090/api/auth/signIn`에 username, password를 넣어서 호출하면 토큰이 발급된다.  
-> 회원 가입 이나 로그인 후에 Authorization 헤더에 Bearer <토큰> 형태로 넣어서 API를 호출하면 된다.  

---  

- 문제 해결 전략   

---
