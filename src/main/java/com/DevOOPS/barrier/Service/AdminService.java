package com.DevOOPS.barrier.Service;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.TyphoonInfoDTO;
import com.DevOOPS.barrier.DTO.WallDTO;
import com.DevOOPS.barrier.DTO.dto;
import com.DevOOPS.barrier.Exception.TyphoonInfoNullException;
import com.DevOOPS.barrier.Exception.TyphoonSearchException;
import com.DevOOPS.barrier.Mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
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

    @Value("${api.EncodedKey")
    private String EncodedServiceKey;
    private WebClient webClient = WebClient.create("http://192.168.0.20:8080/data");
    //192.168.0.20:8080/data

    public void createAdmin(dto dt) {
        mapper.createAdmin(dt);
    }

    public void deleteAdmin(String adminId) {
        dt.getAdminId().equals(adminId);
        mapper.deleteAdmin(adminId);
    }

    public List<ReportAPIdto> load_save() throws TyphoonSearchException { //
        String result = "";
        int HttpStatus = 0;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("stnId", "UTF-8") + "=" + URLEncoder.encode("143", "UTF-8")); /*지점코드 *하단 지점코드 자료 참조*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode("20230329", "UTF-8")); /*시간(년월일)(데이터 생성주기 : 시간단위로 생성)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode(tmToday, "UTF-8")); /*시간(년월일) (데이터 생성주기 : 시간단위로 생성)*/

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
            List<WallDTO> wallDTOList = new ArrayList<WallDTO> ();


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
                String regionData = mapper.RegionData(stnId);

                log.info("배열의 " + i + "번째 요소");
                log.info("stnId : " + stnId + "\ttitle : " + title + "\ttmFc : " + date + "\ttmSeq : " + tmSeq + "\tregion : " + regionData);

                reportAPIdto1 = new ReportAPIdto(stnId, title, date, tmSeq, regionData);
                mapper.ReportAPICall(reportAPIdto1); //mapper 클래스에 사용. //dao로 바꿔야 함.

                wallDTOList.add(new WallDTO(true, stnId));
                reportAPIdtoList.add(new ReportAPIdto(stnId, title, date, tmSeq, regionData));

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

            Mono<String> response = webClient.post()
                    .uri("/test")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(wallDTOList))
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = response.block();
            log.info(responseBody);

        } catch (Exception e) {
            throw new TyphoonSearchException("검색된 데이터가 없습니다.");

        }

        return reportAPIdtoList;
    } //to FE

    //to IoT
    public List<WallDTO> IoTReportAPI() throws TyphoonSearchException {
        String result = "";
        String excludeWord = "강풍";
        int HttpStatus = 0;
        List<WallDTO> wallDTOList = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("stnId", "UTF-8") + "=" + URLEncoder.encode("143", "UTF-8")); /*지점코드 *하단 지점코드 자료 참조*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode("20230329", "UTF-8")); /*시간(년월일)(데이터 생성주기 : 시간단위로 생성)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode(tmToday, "UTF-8")); /*시간(년월일) (데이터 생성주기 : 시간단위로 생성)*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-type", "application/json");
            log.info("Response Code : " + urlConn.getResponseCode());
            log.info("오늘은 " + tmToday + "");

            BufferedReader bf;
            if (urlConn.getResponseCode() >= 200 && urlConn.getResponseCode() <= 300) {
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

            log.info(sb.toString());
            result = sb.toString();

            //Domain
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(result);
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
            wallDTOList = new ArrayList<WallDTO>();

            log.info(excludeWord + " 정보를 불러옵니다 ...");

            for (int i = 0; i < infoArr.size(); i++) { //for each으로 변경 고려.
                tmp = (JSONObject) infoArr.get(i);
                int stnId = Integer.parseInt(String.valueOf(tmp.get("stnId")));
                String title = String.valueOf(tmp.get("title"));
                if (!title.contains(excludeWord)) {
                    wallDTOList.add(new WallDTO(false, stnId));
                    continue;
                }
                String tmFc = String.valueOf(tmp.get("tmFc"));
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                Date date = format.parse(tmFc);

                System.out.println(date);

                int tmSeq = Integer.parseInt(String.valueOf(tmp.get("tmSeq")));
                wallDTOList.add(new WallDTO(true, stnId));
            }

            for (WallDTO wallDTO : wallDTOList) {
                log.info(wallDTO.toString());
            }

            Mono<String> response = webClient.post()
                    .uri("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(wallDTOList))
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = response.block();
            log.info(responseBody);

        } catch (NullPointerException e) {
            throw new TyphoonSearchException("검색된 데이터가 없습니다.");

        }
        catch (Exception e) {
            log.info(e.toString());
        }

        return wallDTOList;
    }

    public List<TyphoonInfoDTO> PostTyphoonInfo(String date) throws TyphoonSearchException, TyphoonInfoNullException {
        List<TyphoonInfoDTO> typhoonInfoDTOList;
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TyphoonInfoService/getTyphoonInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + ServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*시간(년월일)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*시간(년월일)*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            log.info("Response Code : " + conn.getResponseCode());
            log.info("오늘은 " + tmToday + "");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            System.out.println(sb.toString());
            String TyphoonInfoResult = sb.toString();

            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(TyphoonInfoResult);
            log.warn("result : " + TyphoonInfoResult);
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
            typhoonInfoDTOList = new ArrayList<>();

            for (int i = 0; i < infoArr.size(); i++) {
                tmp = (JSONObject) infoArr.get(i);
                String img = String.valueOf(tmp.get("img"));
                String tmFc = String.valueOf(tmp.get("tmFc"));
                String typSeq = String.valueOf(tmp.get("typSeq"));
                String tmSeq = String.valueOf(tmp.get("tmSeq"));
                String typTm = String.valueOf(tmp.get("typTm"));
                float typLat = Float.parseFloat(String.valueOf(tmp.get("typLat")));
                float typLon = Float.parseFloat(String.valueOf(tmp.get("typLon")));
                String typLoc = String.valueOf(tmp.get("typLoc"));
                String typDir = String.valueOf(tmp.get("typDir"));
                float typSp = Float.parseFloat(String.valueOf(tmp.get("typSp")));
                float typPs = Float.parseFloat(String.valueOf(tmp.get("typPs")));
                float typWs = Float.parseFloat(String.valueOf(tmp.get("typWs")));
                float typ15 = Float.parseFloat(String.valueOf(tmp.get("typ15")));
                float typ25 = Float.parseFloat(String.valueOf(tmp.get("typ25")));
                String typName = String.valueOf(tmp.get("typName"));
                String typEn = String.valueOf(tmp.get("typEn"));
                String rem = String.valueOf(tmp.get("rem"));
                String other = String.valueOf(tmp.get("other"));

                log.info("배열의 " + i + "번째 요소");
                typhoonInfoDTOList.add(new TyphoonInfoDTO(img, tmFc, typSeq, tmSeq, typTm, typLat, typLon, typLoc, typDir, typSp, typPs,
                        typWs, typ15, typ25, typName, typEn, rem, other));

            }

            for (TyphoonInfoDTO typhoonInfoDTO : typhoonInfoDTOList) {
                log.info(typhoonInfoDTO.toString());
            }

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new TyphoonInfoNullException("NullPointerException : 비어있는 데이터에 접근하였습니다.");
        } catch (Exception e) {
            throw new TyphoonSearchException("검색된 데이터가 없습니다.");
        }

        return typhoonInfoDTOList;
    }

    public int ServerTime() {
        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int formattedNow_1 = Integer.parseInt(time.format(formatter));

        return formattedNow_1;
        }
    }