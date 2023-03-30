package com.DevOOPS.barrier.Service;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.WallDTO;
import com.DevOOPS.barrier.DTO.dto;
import com.DevOOPS.barrier.Exception.TyphoonSearchException;
import com.DevOOPS.barrier.Mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//private과 protected과 public 구분해야 함.
@Service //Bean에 등록하는 annotation. 기본으로 싱글톤으로 등록한다 (유일하게 하나만 등록해서 공유한다)
@Slf4j
public class AdminService {
    dto dt;
    ReportAPIdto reportAPIdto;
    @Autowired
    AdminMapper mapper;
    String tmToday = String.valueOf(ServerTime());
    ReportAPIdto reportAPIdto1;
    List<ReportAPIdto> reportAPIdtoList = new ArrayList<>();
    @Value("${api.key}")
    private String ServiceKey;
    private WebClient webClient = WebClient.create("https://f2333ebd-65f8-4202-a1a4-a727ebc010ad.mock.pstmn.io");

    public void createAdmin(dto dt) {
        mapper.createAdmin(dt);
    }

    public void deleteAdmin(String adminId) {
        dt.getAdminId().equals(adminId);
        mapper.deleteAdmin(adminId);
    }

//    @Scheduled(fixedDelay = 360000) //1시간마다 불러옴.
    public List<ReportAPIdto> load_save() throws TyphoonSearchException {
        String result = "";
        int HttpStatus = 0;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("stnId", "UTF-8") + "=" + URLEncoder.encode("143", "UTF-8")); /*지점코드 *하단 지점코드 자료 참조*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode("20220101", "UTF-8")); /*시간(년월일)(데이터 생성주기 : 시간단위로 생성)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode("20220101", "UTF-8")); /*시간(년월일) (데이터 생성주기 : 시간단위로 생성)*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-type", "application/json");
            log.info("Response Code : " + urlConn.getResponseCode());
            log.info("오늘은 " + tmToday + "");

            BufferedReader bf;
            if (urlConn.getResponseCode() >= 200 && urlConn.getResponseCode() <= 300) { //try catch 형태로 처리해야 함.
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

            urlConn.disconnect();

            log.info(sb.toString()); //log.info로 수정.
            result = sb.toString();

            //Domain
            JSONParser jsonParser = new JSONParser(); //오류 해결해야 함.
            JSONObject obj = (JSONObject) jsonParser.parse(result); //하나씩 출력. Parsing 문제.
            log.warn("result : " + result);
            log.warn("obj : " + obj);
            JSONObject parse_response = (JSONObject) obj.get("response");
            log.warn("response : " + parse_response);
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            log.warn("body : " + parse_body);
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            log.info("parse_items" + parse_items);
            JSONArray infoArr = (JSONArray) parse_items.get("item");
            log.info("itemResult" + infoArr);


            JSONObject tmp;
            List<WallDTO> wallDTOList = new ArrayList<WallDTO>();


            for (int i = 0; i < infoArr.size(); i++) { //for each으로 변경 고려.
                tmp = (JSONObject) infoArr.get(i);
                int stnId = Integer.parseInt(String.valueOf(tmp.get("stnId")));
                String title = String.valueOf(tmp.get("title"));
//                if (!title.contains("태풍")) {
//                    continue;
//
//                }
                String tmFc = String.valueOf(tmp.get("tmFc"));
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                Date date = format.parse(tmFc);

                System.out.println(date);

                int tmSeq = Integer.parseInt(String.valueOf(tmp.get("tmSeq")));

                log.info("배열의 " + i + "번째 요소");
                log.info("stnId : " + stnId + "\ttitle : " + title + "\ttmFc : " + date + "\ttmSeq : " + tmSeq);

                reportAPIdto1 = new ReportAPIdto(stnId, title, date, tmSeq);
                mapper.ReportAPICall(reportAPIdto1); //mapper 클래스에 사용. //dao로 바꿔야 함.

                wallDTOList.add(new WallDTO(true, stnId));
                reportAPIdtoList.add(new ReportAPIdto(stnId, title, date, tmSeq));

            }

            /*
            {"response":{"header":{"resultCode":"00","resultMsg":"NORMAL_SERVICE"},
                "body":{"dataType":"JSON","items":{"item":[
                    {"stnId":"184","title":"[특보] 제01-3호 : 2023.01.03.16:00 / 풍랑주의보 해제 (*)","tmFc":202301031600,"tmSeq":3},
                    {"stnId":"184","title":"[특보] 제01-2호 : 2023.01.03.14:00 / 풍랑주의보 해제 (*)","tmFc":202301031400,"tmSeq":2},
                    {"stnId":"184","title":"[특보] 제01-1호 : 2023.01.02.20:30 / 풍랑주의보 발표(*)","tmFc":202301022030,"tmSeq":1} ] }
                    ,"pageNo":1,"numOfRows":10,"totalCount":3}}}
             */
            for (WallDTO wallDTO : wallDTOList) {
                log.info(wallDTO.toString());
            }
//            webClient.post()
//                    .uri("/test")
//                    .bodyValue(wallDTOList)
//                    .retrieve()
//                    .bodyToMono(void.class)//메시지를 받아 옴.
//                    .block();
//
//            Mono<String> response = webClient.post()
//                    .uri("/test")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(BodyInserters.fromValue(wallDTOList))
//                    .retrieve()
//                    .bodyToMono(String.class);

//            String responseBody = response.block();
//            log.info(responseBody);

        } catch (Exception e) {
            throw new TyphoonSearchException("검색된 데이터가 없습니다.");

        }

        return reportAPIdtoList;
    }

    //dao 태풍 정보를 그대로 전달하는 메서드를 만들어야 함.

    //다른 6시간 메서드를 적어야 함. 컨트롤러단에서 되는지 확인해야 함.

    public int ServerTime() {
        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int formattedNow_1 = Integer.parseInt(time.format(formatter));

        return formattedNow_1;

        }
    }