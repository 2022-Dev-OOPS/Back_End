package com.DevOOPS.barrier.Service;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.dto;
import com.DevOOPS.barrier.Mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//private과 protected과 public 구분해야 함.
@Service //Bean에 등록하는 annotation. 기본으로 싱글톤으로 등록한다 (유일하게 하나만 등록해서 공유한다)
@Slf4j
public class AdminService {
    dto dt;
    ReportAPIdto reportAPIdto;
    @Autowired
    AdminMapper mapper;

    @Value("${api.key}")
    private String ServiceKey;

    public void createAdmin(dto dt) {
        mapper.createAdmin(dt);
    }
    public void deleteAdmin(String adminId) {
        dt.getAdminId().equals(adminId);
        mapper.deleteAdmin(adminId);
    }

    public int load_save(String tmTo, String tmFrom) {
        String result = "";
        int HttpStatus = 0;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("stnId", "UTF-8") + "=" + URLEncoder.encode("184", "UTF-8")); /*지점코드 *하단 지점코드 자료 참조*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode(tmTo, "UTF-8")); /*시간(년월일)(데이터 생성주기 : 시간단위로 생성)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode(tmFrom, "UTF-8")); /*시간(년월일) (데이터 생성주기 : 시간단위로 생성)*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response Code : " + urlConn.getResponseCode());

            BufferedReader bf;
            if(urlConn.getResponseCode() >= 200 && urlConn.getResponseCode() <= 300) {
                bf = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            } else {
                bf = new BufferedReader(new InputStreamReader(urlConn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
            bf.close();
            HttpStatus = urlConn.getResponseCode();
            urlConn.disconnect();
            System.out.println(sb.toString());
            result = sb.toString();

            //Domain
            JSONParser jsonParser = new JSONParser(); //오류 해결해야 함.
             JSONObject obj = (JSONObject) jsonParser.parse(result); //하나씩 출력. Parsing 문제.
             JSONObject parse_response = (JSONObject) obj.get("response");
//                 responseResult = (String)response.get("body");
//                log.info("responseResult" + responseResult); //로그 콘솔 출력.
             JSONObject parse_body = (JSONObject) parse_response.get("body");
//                 bodyResult = (String)body.get("items");
//                log.info("bodyResult" + bodyResult);
             JSONObject parse_items = (JSONObject) parse_body.get("items");
//                 itemResult = (String)items.get("title");
//                log.info("ItemResult" + itemResult);
             JSONArray infoArr = (JSONArray) parse_items.get("item");

            //Unexpected character (<) at position 0. Parsing Error 해결해야 함.
            //오류(HttpStatus) 뜨면 오류 안내 페이지로

            JSONObject tmp;
            for(int i=0; i<infoArr.size(); i++) { //for each으로 변경 고려.
                tmp = (JSONObject) infoArr.get(i);
                int stnId = (int) tmp.get("stnId");
                String title = (String) tmp.get("title");
                String tmFc = (String) tmp.get("tmFc");
                int tmSeq = (int) tmp.get("tmSeq");

                System.out.println("배열의" + i + "번째 요소");
                System.out.println("stnId : " + stnId + "\ttitle : " + title + "\ttmFc : " + tmFc + "\ttmSeq : " + tmSeq);

//                ReportAPIdto reportAPIdto1 = new ReportAPIdto(i, stnId, title, tmFc, tmSeq);
//                mapper.ReportAPICall(reportAPIdto1);
            }


            /*
            {"response":{"header":{"resultCode":"00","resultMsg":"NORMAL_SERVICE"},
                "body":{"dataType":"JSON","items":{"item":[
                    {"stnId":"184","title":"[특보] 제01-3호 : 2023.01.03.16:00 / 풍랑주의보 해제 (*)","tmFc":202301031600,"tmSeq":3},
                    {"stnId":"184","title":"[특보] 제01-2호 : 2023.01.03.14:00 / 풍랑주의보 해제 (*)","tmFc":202301031400,"tmSeq":2},
                    {"stnId":"184","title":"[특보] 제01-1호 : 2023.01.02.20:30 / 풍랑주의보 발표(*)","tmFc":202301022030,"tmSeq":1} ] }
                    ,"pageNo":1,"numOfRows":10,"totalCount":3}}}
             */
            //도메인을 위 형식으로 (response 안에 body 안에 stnId, title, tmFc, tmSeq)
            //도메인을 출력하면 위의 형식과 똑같음.



        } catch (Exception e) {
            log.info(e.toString());
            // printstackTrace 필요 없음, 로그(Warning, Error)
            // 출력해야 함. 테스트 코드 작성 해서 exception마다 처리해야 함.
        }
    return HttpStatus;
    }
/*
    public void TyphoonAnalyzed() {
        int TyphoonAnalyzed = 0; //태풍 주의보 : 1, 태풍 특보 : 2, 특보 구문 분석 후 숫자 코드 추가할 예정.

        for(int j=0; j<infoArr.size(); j++) {
            JSONObject tmp = (JSONObject) infoArr.get(j);
            String title = (String) tmp.get("title");
            String word = null;

            if(title.contains("태풍주의보")) {
                TyphoonAnalyzed = 1;
                log.info("태풍주의보");
            }
            if(title.contains("태풍특보")) {
                TyphoonAnalyzed = 2;
                log.info("태풍특보");
            }
            else {
                log.info("태풍이 발생하지 않았습니다.");
            }
        }*/


    public int ServerTime() {
        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int formattedNow_1 = Integer.parseInt(time.format(formatter));

        return formattedNow_1;

        }
    }



