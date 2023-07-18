![image](https://github.com/2022-Dev-OOPS/Back_End/assets/86722532/e3140beb-34ad-485d-a3c6-6b7ed8a53bed)
![image](https://github.com/2022-Dev-OOPS/Back_End/assets/86722532/2c708d0a-7f16-4501-8fc8-0b3f5333c2f4)
# Dev.OOPS! Back-End

## 파트 소개
공공데이터 포털에서 기상청이 제공하는 API를 이용하여 각 서버 (FE, IoT)에 필요한 정보를 가공하여 보내주는 역할을 맡고 있습니다.

## 주요 특징
실시간으로 기상 정보를 받을 수 없다는 태풍의 특징에 기반하여, 본 프로젝트는 **가상 태풍 환경 서버**와 **실제 태풍 환경 서버**로 나누어 개발을 진행하였습니다.

#ERD
![image](https://github.com/2022-Dev-OOPS/Back_End/assets/86722532/97e656d8-bcce-49c4-ad68-c71de1d0156f)

# 기술 스택 및 환경
- Java 17
- Framework : Springboot
- DB : MariaDB
- DB Server : AWS RDS
- Log 관리 : Logback
- SQL Mapper : Mybatis
- 단위 테스트 : Junit5
- 성능 테스트 : Jmeter
- api-test : PostMan
- lombok

## 작동 과정


# 가상 태풍 환경 서버
1. 작성 필요
2.
3. 
# 실제 태풍 환경 서버
### FE로의 예특보 관련 데이터 전송 ( FE에서 요청 수신 시 1 - 3 수행)

1. 기상청 공공데이터 API 서버에서 예,특보 관련 정보 요청 및 수신 
    1. 요청 시각에 따라 수신하는 정보가 달라짐.
    2. 오늘 기준 최대 5일까지의 정보만 불러올 수 있음.
    3. 지역 번호를 매개변수로 주어 원하는 지역의 정보를 받을 수 있음.
    4. JSON / XML 선택 가능 ( Java 내에서 다루기 위해 JSON을 선택)
2. JSONArray 형태로 데이터들을 묶은 후, 특보번호, 특보명, 특보 발효 시각 등 FE에 맞는 데이터 형식을 파싱함.
3. GET 방식으로 통신함.
    1. FE에서 요청이 들어오면 BE에서 보내주는 형식임.

### IoT Part로의 차수벽 명령 데이터 전송

1. 기상청 공공데이터 API 서버에서 예,특보 관련 정보 요청 및 수신 (실제 시간 1시간마다 요청함)
2. 만약 차수벽 설치 지역(부산, 마산 등)에 태풍 관련 특보가 발효됨. 
    1. 다른 풍랑, 호우, 폭우 등 태풍 간접적 특보는 고려하지 않음.
3.  BE에서 IoT Part로 차수벽을 가동하라는 데이터를 전송함. (Activation, Deactivation)

### FE로의 CCTV 영상 데이터 전송

1. XAMPP를 통해 웹 서버를 열어줌. (Tomcat)
2. IoT의 캠 서버에 접속(RSTP)하여 HLS를 통해 세그먼트 파일(stream0001.ts)과 메타데이터 파일(stream.m3u8)을 만듦.
    1. 세그먼트 파일 : 실시간을 구현하기 위해 설정된 시간만큼 영상데이터를 담고 있음.
        1. 본 프로젝트는 10초로 설정해놨으며, 이전 세그먼트 파일과 타임스탬프가 이어짐.
    2. 메타데이터 파일 : 위 세그먼트 파일을 시간 순서대로 나열해놓은 메타데이터 파일.
        - 이 파일을 통하여 세그먼트 파일에 접근 및 영상 데이터를 볼 수 있음.
3. 80번 포트를 이용하여 열어둔 웹 서버에 FE가 접속하여 stream.m3u8을 Dashboard에 정보를 게시함.

<img width="727" alt="image" src="https://github.com/2022-Dev-OOPS/Back_End_Integrated/assets/86722532/af04648a-ef2c-442c-9bfe-b8b69444c9d9">
사진 1. 공공데이터 포털에 요청하여 받아온 데이터 모습. (오늘 기준 3일 전 데이터만 불러올 수 있음.)
