# 지자체 협약 지원 정보 보기
 
- 구현 환경  
Spring boot    
Java 1.8  
Gradle  
Undertow  
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
-> https://github.com/sung9020/local_government_support_info/releases 

---  

- API 명세를 보기위한 Swagger 주소  
-> localhost:9090/swagger-ui.html   

---  

- Swagger 테스트 시에 주의점  
-> 회원가입, 로그인 API 외에는 토큰 인증을 거쳐야 API 호출이 가능하다.  
-> Authorize 버튼을 누른 후에 Bearer <토큰> 을 넣어주면 API 호출이 가능해진다.  

---  

- API 요청 프로세스  
-> 회원 가입 및 로그인을 하지 않으면 구현된 API를 호출 할 수 없다.  
-> 회원 가입을 위해 `localhost:9090/api/auth/signUp`을 호출하면 토큰이 발급된다.  
-> 이미 회원 가입을 하였으면, `localhost:9090/api/auth/signIn`에 username, password를 넣어서 호출하면 토큰이 발급된다.  
-> 회원 가입 이나 로그인 후에 Authorization 헤더에 Bearer <토큰> 형태로 넣어서 API를 호출하면 된다.  

---  

- 문제 해결 전략   
1. 파일 저장할 때 엔티티를 정해야한다. 지자체 정보, 협약 지원정보, 유저 정보를 각각 엔티티 클래스로 작성하여 DB에 싱크한다.  

2. csv의 지원금액이나 이차보전의 경우 텍스트와 숫자가 섞여있다. 이를 숫자와 단위로 걸러내기 위해서는 문자열 파싱이 필요하다.  

3. 쉽게 문자열과 숫자를 분리하기 위해서는 정규표현식을 사용. 이차보전, 지원금액, 사용용도 별로 패턴을 정하여 사용하였다.

4. 지자체 지원정보를 지원금액 별 내림차순으로 보기 - 문자열 파싱을 통해 지원금액을 number타입으로 뽑아낸다.  
그 후 java 8 stream을 활용하여 지원금액이 같을 경우에 comparing 함수를 체이닝하여 이차보전 비율로 비교할 수 있도록 하였다.

5. 지차체 지원정보의 이차보전 비율 최소 지자체 구하기 - 문자열 파싱을 통해 이차보전을 number타입으로 뽑아낸다. 
비율에는 최대와 최소의 오차가 존재하므로 평균을 구하여 평균 중에 최소인 지자체를 선별한다. 

6. 지자체 지원정보 추천정보 구하기 - 지자체 간의 거리 점수를 위하여 이차원 배열을 만들고 이차원 배열에 랜덤으로 1~100까지 거리점수를 부여한다.  
지자체 A, B가 있다면 A-B, B-A를 같은 점수를 줘서 두 지자체 간의 거리를 명시한다.  
거리순으로 지자체 리스트를 정렬하고, 그 중에서 문자열 파싱으로 뽑아낸 이차보전, 지원금액, 사용용도와 일치하는 조건의 지차체 중에 가장 우선순위가 높은 지자체를 요청자에게 보여준다.   

---
