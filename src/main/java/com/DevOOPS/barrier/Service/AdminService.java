package com.DevOOPS.barrier.Service;

import com.DevOOPS.barrier.DTO.*;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.*;

import static java.lang.Math.*;


@Service
@Slf4j
public class AdminService {
    @Autowired
    AdminMapper mapper;
    String tmToday = String.valueOf(ServerTime());
    String minusTmToday = String.valueOf(MinusServerTime());
    ReportAPIdto reportAPIdto1;
    @Value("${api.EncodeKey}")
    private String EncodeServiceKey;

    @Value("${api.DecodeKey}")
    private String DecodeServiceKey;

    @Value("${api.enterAddress}")
    private String enterAddress;
    private double[] wallLatLon = new double[] { 35.1516053, 129.1170532 };
    private WebClient webClient = WebClient.create(enterAddress);

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public boolean wallActivation(double wallLatitude, double wallLongtitude, double typLatitude, double typLongtitude, int rad15) {
        double theta = wallLongtitude - typLongtitude;
        double dist = Math.sin(deg2rad(wallLatitude)) * Math.sin(deg2rad(typLatitude)) + Math.cos(deg2rad(wallLatitude  )) * Math.cos(deg2rad(typLatitude)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;


        if (rad15 >= dist) {
            System.out.println("태풍 현 시점 초속 15m/s의 영향 범위 : " + rad15 + " dist : " + dist + "영향 범위 안에 있습니다");
            return true;
        }
        else {
            System.out.println("태풍 현 시점 초속 15m/s의 영향 범위 : " + rad15 + " dist : " + dist + "영향 범위 안에 없습니다");
            return false;
        }
    }

    public List<TypFcst> getTyphoonFcst(String getAnnounceTime, String getTyphoonSeq) {
        List<TypFcst> TypFcstDTOList = new ArrayList<>();

        System.out.print(getAnnounceTime + " + " + getTyphoonSeq);

        try{
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TyphoonInfoService/getTyphoonFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + DecodeServiceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(getAnnounceTime, "UTF-8")); /*발표시각(년월일시분)태풍정보서비스의 호출 결과 중 발표시간 값 참조하여 입력*/
        urlBuilder.append("&" + URLEncoder.encode("typSeq","UTF-8") + "=" + URLEncoder.encode(getTyphoonSeq, "UTF-8")); /*태풍번호*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
        String result;
        result = sb.toString();

        JSONParser jsonParser = new JSONParser();
        JSONObject obj = (JSONObject) jsonParser.parse(result); //하나씩 출력. Parsing 문제.
        JSONObject parse_response = (JSONObject) obj.get("response");
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        JSONArray infoArr = (JSONArray) parse_items.get("item");
        System.out.println("infoArr Size: " + infoArr.size());

        JSONObject temp = new JSONObject();

        String get_dir;
        String get_ed15;
        double get_lat = -1;
        double get_lon = -1;
        int get_rad15 = -1;
        String get_tm;
        String get_tmFc;
        int get_ws;
            // list 추가하기.

        for (int i = 0; i < infoArr.size(); i++) { //for each으로 변경 고려.
            temp = (JSONObject) infoArr.get(i);
            get_dir = String.valueOf(temp.get("dir"));
            get_ed15 = String.valueOf(temp.get("ed15"));
            get_lat = Double.parseDouble(String.valueOf(temp.get("lat")));
            get_lon = Double.parseDouble(String.valueOf(temp.get("lon")));
            get_rad15 = Integer.parseInt(String.valueOf(temp.get("rad15")));
            get_tm = String.valueOf(temp.get("tm"));
            get_tmFc = String.valueOf(temp.get("tmFc"));
            get_ws = Integer.parseInt(String.valueOf(temp.get("ws")));
            log.info(get_dir, get_ed15, get_lat, get_lon, get_rad15, get_tm, get_tmFc, get_ws);
            TypFcstDTOList.add(new TypFcst(get_dir, get_ed15, get_lat, get_lon, get_rad15, get_tm, get_tmFc, get_ws));
            System.out.println(TypFcstDTOList.toString());
        }

        boolean selectWallActivation = wallActivation(35.1516053, 129.1170532, get_lat, get_lon, get_rad15);
        System.out.println(selectWallActivation);


        } catch (Exception e) {
            e.toString();
        }
        return TypFcstDTOList;
    }
    public String getTyphoonInfoList() throws UnsupportedEncodingException {
        String reformData = null;
        String returnResult = "";
        StringBuilder sb = new StringBuilder();
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TyphoonInfoService/getTyphoonInfoList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + DecodeServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode("230729", "UTF-8")); /*발표시각*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            reformData = sb.toString();
            //const jsonString = `{"key": "안녕하세요.\n줄을 바꾸고\n반갑습니다"}`;
            //const replaceJsonString = jsonString.replace(/\n/g, '\\n');
            //const jsonObj = JSON.parse(replaceJsonString);
////            console.log(jsonObj.key);
//            stirng s = "json 형식의 데이터 값";
//            JSONObject jo = new JSONObject(s);

//            reformData = reformData.replace("/\n/g", "\\n");

            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(reformData); //하나씩 출력. Parsing 문제.
            JSONObject parse_response = (JSONObject) obj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            JSONArray infoArr = (JSONArray) parse_items.get("item");

            JSONObject temp = new JSONObject();

            String getAnnounceTime = null;
            String getTyphoonSeq = null;

            temp = (JSONObject) infoArr.get(0);

            getAnnounceTime = String.valueOf(temp.get("announceTime"));
            getTyphoonSeq = String.valueOf(temp.get("typhoonSeq"));
            returnResult = getAnnounceTime + ","+getTyphoonSeq;
            System.out.println(returnResult);
        } catch (Exception e) {
            log.info(e.toString());
        }
        return returnResult;
    }

    @Async
    public StringBuilder apiServerRequest() throws TyphoonSearchException {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/WthrWrnInfoService/getWthrWrnList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + DecodeServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
//            urlBuilder.append("&" + URLEncoder.encode("stnId", "UTF-8") + "=" + URLEncoder.encode("184", "UTF-8")); /*지점코드 *하단 지점코드 자료 참조*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode(minusTmToday, "UTF-8")); /*시간(년월일)(데이터 생성주기 : 시간단위로 생성)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode(tmToday, "UTF-8")); /*시간(년월일) (데이터 생성주기 : 시간단위로 생성)*/


            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
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

            return sb;
        } catch (Exception e) {
            log.info(e.toString());
            throw new TyphoonSearchException("검색된 데이터가 없습니다.");
        }
    }

    public List<ReportAPIdto> load_save () {
        String result = "";
        List<ReportAPIdto> reportAPIdtoList = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder();
            sb = apiServerRequest();
            result = sb.toString();
            log.info(result);

            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(result); //하나씩 출력. Parsing 문제.
            JSONObject parse_response = (JSONObject) obj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            JSONArray infoArr = (JSONArray) parse_items.get("item");

            JSONObject tmp = new JSONObject();
            int stnId;
            String title;
            String tmFc;
            Date date;
            int tmSeq;
            String regionData;


            for (int i = 0; i < infoArr.size(); i++) { //for each으로 변경 고려.
                tmp = (JSONObject) infoArr.get(i);
                stnId = Integer.parseInt(String.valueOf(tmp.get("stnId")));
                title = String.valueOf(tmp.get("title"));
                String[] tokens = title.split("/");
                String WtrWrn = tokens[0];
                String[] WtrWrnName = WtrWrn.split(":");
                tmFc = String.valueOf(tmp.get("tmFc"));

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                date = format.parse(tmFc);

                tmSeq = Integer.parseInt(String.valueOf(tmp.get("tmSeq")));
                regionData = mapper.RegionData(stnId);

                log.info("region : " + regionData + "\t특보명 : " + WtrWrnName[0] + "\t특보 내용 : " + tokens[1] + "\ttmFc : " + date);
                reportAPIdtoList.add(new ReportAPIdto(stnId, date, tmSeq, regionData, WtrWrnName[0], tokens[1]));

//                reportAPIdto1 = new ReportAPIdto(stnId, date, tmSeq, regionData, tokens[0], tokens[1]);
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
        } catch (Exception e) {
            log.info(e.toString());
        }
        return reportAPIdtoList;
    }


//    @Scheduled(fixedDelay = 10000)
    public WallDTO IoTReportAPI() {
        String result = "";
        String excludeWord = "호우";
        String Activated = "발표";
        String Deactivated = "해제";

        List<WallDTO> wallDTOList = null;
        WallDTO wallDTOtemp = new WallDTO(false, false, 0);

        try {
            StringBuilder sb = new StringBuilder();
            sb = apiServerRequest();

            log.info(sb.toString());
            result = sb.toString();

            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(result);
            JSONObject parse_response = (JSONObject) obj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            JSONArray infoArr = (JSONArray) parse_items.get("item");
            log.info("itemResult" + infoArr);

            JSONObject tmp;
            wallDTOList = new ArrayList<WallDTO>();

            log.info(excludeWord + " 정보를 불러옵니다 ...");

            for (int i = 0; i < infoArr.size(); i++) { //for each으로 변경 고려.
                tmp = (JSONObject) infoArr.get(i);
                int stnId = Integer.parseInt(String.valueOf(tmp.get("stnId")));
                String title = String.valueOf(tmp.get("title"));
                if (title.contains(excludeWord) && title.contains(Activated)) {
                    wallDTOList.add(new WallDTO(true, false, stnId));
                } else if (title.contains(excludeWord) && title.contains(Deactivated)) {
                    wallDTOList.add(new WallDTO(false, true, stnId));
                } else continue;
            }

            Collections.reverse(wallDTOList); //오래된 날짜부터 최신 날짜 순.

            for (WallDTO wallDTO : wallDTOList) {
                log.info(wallDTO.toString());
            }

            wallDTOtemp = wallDTOList.get(0); //최신 해당 특보를 불러 옴.

            Mono<String> response = webClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(wallDTOtemp))
                    .retrieve()
                    .bodyToMono(String.class);
        }
        catch (Exception e) {
            log.info(e.toString());
        }
        return wallDTOtemp;
    }

    @Async
    public  List<TyphoonInfoDTO>  PostTyphoonInfo() throws TyphoonSearchException, TyphoonInfoNullException {
        List<TyphoonInfoDTO> typhoonInfoDTOList;
        String resultTypPower = "";
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TyphoonInfoService/getTyphoonInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + DecodeServiceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc", "UTF-8") + "=" + URLEncoder.encode(tmToday, "UTF-8")); /*시간(년월일)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc", "UTF-8") + "=" + URLEncoder.encode(tmToday, "UTF-8")); /*시간(년월일)*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            log.info("Response Code : " + conn.getResponseCode());
            log.info("오늘은 date = " + tmToday + "");
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
//            Mono<String> response = webClient.post()
//                    .uri("/test")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(BodyInserters.fromValue(typhoonInfoDTOList))
//                    .retrieve()
//                    .bodyToMono(String.class);
//            String responseBody = response.block();
//            log.info(responseBody);

            return typhoonInfoDTOList;
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
            log.info(e.toString());
            throw new TyphoonSearchException("검색된 데이터가 없습니다.");
        }
//        if (TypPower >= 17 && TypPower < 25)
//            resultTypPower = "약";
//        else if (TypPower >= 25 && TypPower < 33)
//            resultTypPower = "중";
//        else if (TypPower >= 33 && TypPower < 44)
//            resultTypPower = "강";
//        else if (TypPower >= 44 && TypPower < 54)
//            resultTypPower = "매우 강";
//        else if (TypPower > 54)
//            resultTypPower = "초강력";
//        else if (TypPower < 17 || TypPower >= 1000)
//            resultTypPower = "정상 데이터가 아닙니다.";
//        return resultTypPower;
    }

    public int ServerTime() {
        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int formattedNow_1 = Integer.parseInt(time.format(formatter));

        return formattedNow_1;
    }

    public int MinusServerTime() {
        LocalDate minusTime = LocalDate.now();
        minusTime = minusTime.minusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int formattedNow_2 = Integer.parseInt(minusTime.format(formatter));

        return formattedNow_2;
    }
}