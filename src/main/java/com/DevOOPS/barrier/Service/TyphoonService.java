package com.DevOOPS.barrier.Service;

import com.DevOOPS.barrier.DTO.TyphoonAPIdto;
import com.DevOOPS.barrier.Mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Service
@Slf4j
public class TyphoonService {

    TyphoonAPIdto typhoonAPIdto;

    @Resource(name = "sqlSessionTemplate")
    private SqlSession session;
    @Autowired
    AdminMapper mapper;

    public int load_Typhoon(String typtmTo, String typFrom) {
        String result = "";
        int HttpStatus = 0;

        try{
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TyphoonInfoService/getTyphoonInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=wi0Fe0N5C02cEenuZ7R%2Bj3vBrM9m9dV6dPGVZkHwbhdCMdThzXRetUoivRZDsvVgcj65TCbmVpba9lNOdXGbFg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("fromTmFc","UTF-8") + "=" + URLEncoder.encode(typtmTo, "UTF-8")); /*시간(년월일)*/
            urlBuilder.append("&" + URLEncoder.encode("toTmFc","UTF-8") + "=" + URLEncoder.encode(typFrom, "UTF-8")); /*시간(년월일)*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            rd = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = rd.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray infoArr = (JSONArray) items.get("item");
            //body문제 - jsonparser josn쪼개기.

            JSONObject tmp;
            for(int i=0; i<infoArr.size(); i++) {
                tmp = (JSONObject) infoArr.get(i);
                String tmFc = (String) tmp.get("tmFc");
                int tmSeq = (int) tmp.get("tmSeq");
                int typSeq = (int) tmp.get("typSeq");
                String typEn = (String) tmp.get("typEn");
                String typName = (String) tmp.get("typName");
                String typTm = (String) tmp.get("typTm");
                float typLat = (float) tmp.get("typLat");
                float typLon = (float) tmp.get("typLon");
                String typDir = (String) tmp.get("typDir");
                int typWs = (int) tmp.get("typWs");
                int typ25 = (int) tmp.get("typ25");

                System.out.println("배열의" + i + "번째 요소");
                System.out.println("tmFc : " + tmFc + "\ttmSeq : " + tmSeq + "\ntypSeq : " + typSeq
                        + "\ttypEn : " + typEn + "\ttypName : " + typName + "\ttypTm : " + typTm
                        + "\ntypLat : " + typLat + "\ttypLon : " + typLon + "\ttypDir : " + typDir
                        + "\ntypWs : " + typWs + "\ttyp25 : " + typ25);
                //새로운 dTO를 만들어서 원하는 값만 return해주기 + log도 출력해주기
                //새로운 dto에 생성자를 만들어서 그대로 return해주는 방법
            }
        } catch (Exception Te) {
            log.info(Te.toString());
        }
        return 1; //http
    }
    public int ServerTime() {
        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        int formattedNow_1 = Integer.parseInt(time.format(formatter));

        return formattedNow_1;

    }
}
