package com.DevOOPS.barrier.Controller;

import com.DevOOPS.barrier.DTO.PastTypdto;
import com.DevOOPS.barrier.DTO.TypListdto;
import com.DevOOPS.barrier.Service.PastTypService;
import com.DevOOPS.barrier.Status.Message;
import com.DevOOPS.barrier.Status.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
@Slf4j
@RequestMapping("/api/past")
public class PastTypController {
    private final double EARTH_RADIUS = 6371;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime presentDate;
    private LocalDateTime lastDate;
    private int randomIdx;
    private int count = 9;
    private double typLat;
    private double typLon;
    private double korLat = 35.9065;
    private double korLon = 131.8725;
    private double korDis = 303.68;
    private int aftertime;
    private int typrad;
    private String power;

    private String preWallUpDown = "wallAlertDeactivation";
    @Autowired
    PastTypService pastTypService;

    @Value("${api.enterAddress}")
    private String enterAddress;
    private WebClient client = WebClient.create(enterAddress);

    private int getRandomValue(int minIdx, int maxIdx){
        Random random = new Random();
        return random.nextInt(maxIdx - minIdx + 1) +minIdx;
    }

    public  double getDistance(double lat1, double lon1, double lat2, double lon2){
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2)* Math.sin(dLat/2)+ Math.cos(Math.toRadians(lat1))* Math.cos(Math.toRadians(lat2))* Math.sin(dLon/2)* Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = EARTH_RADIUS* c;    // Distance in km
        return d;
    }

    @GetMapping("/information/{idx}")
    public Message getRandomTable(@PathVariable(value = "idx")int idx){
        log.info("idx: " + String.valueOf(idx));
        log.info("count: " + String.valueOf(count));
        if (idx == 0 || count == 7) {
            int minIdx = 1;
            int maxIdx = 76;
            randomIdx = getRandomValue(minIdx, maxIdx);
            TypListdto typListdto = pastTypService.getTypList(randomIdx);
            if(typListdto == null){
                Message message = new Message(StatusEnum.NOT_FOUND, "Object not found", typListdto);
                presentDate = presentDate.now();
                count = 0;
                return message;
            }
            presentDate = typListdto.gettypStart();
            lastDate = typListdto.gettypEnd();
            log.info(String.valueOf(presentDate));
            log.info(String.valueOf(lastDate));
            count = 0;
            return getTableByTypDate();
        }
        return getTableByTypDate();
    }

    public Message getTableByTypDate() {
        if(randomIdx > 38){
            presentDate = presentDate.now();
            count++;
            log.info(String.valueOf(count));
            return new Message(StatusEnum.NOT_FOUND, "Object not found", null);
        }
        log.info(String.valueOf(presentDate));
        LocalDateTime date =  presentDate;
        List<PastTypdto> pastTypdtos = pastTypService.getPastTyp(date);
        for(PastTypdto p : pastTypdtos) {
            log.info(p.toString());
        }

        if(pastTypdtos.isEmpty()){
            presentDate = presentDate.minusHours(3);
            LocalDateTime date1 = presentDate;
            List<PastTypdto> pastTypdtos1 = pastTypService.getPastTyp(date1);
            if(pastTypdtos1.isEmpty()){return getRandomTable(0);}
            Message message = new Message(StatusEnum.OK, "Past_Report", pastTypdtos1);
            presentDate = presentDate.plusHours(6);
            return message;
        }
        Message message = new Message(StatusEnum.OK, "Present_Report", pastTypdtos);
        presentDate = presentDate.plusHours(3);
        log.info(String.valueOf(presentDate));
        return message;
    }

    @GetMapping("/information/danger")
    public Message getTypDanger(){
        int danger = 0;
        int checkPoint = 0;
        log.info("이모티콘: "+presentDate.toString());
        presentDate=presentDate.minusHours(3);
        log.info("이모티콘: "+presentDate.toString());
        List<PastTypdto> pastTypdtos = pastTypService.getPastTyp(presentDate);
        if(pastTypdtos.isEmpty())
        {
            presentDate=presentDate.minusHours(3);
            checkPoint = 1;
        }
        pastTypdtos = new ArrayList<>();
        pastTypdtos.addAll(pastTypService.getPastTyp(presentDate));

        for(PastTypdto p:pastTypdtos)
        {
            if(p.getAfter_time()<=24)
            {
                log.info(p.toString());
                try {
                    p.getPower().equals("");
                }catch (Exception e)
                {
                    continue;
                }
                if(p.getPower().equals("강")||p.getPower().equals("매우강")||p.getPower().equals("매우 강")||p.getPower().equals("초강력"))
                {
                    danger=2;
                }
                if(p.getPower().equals("약")&&danger!=2||p.getPower().equals("중")&&danger!=2)
                {
                    danger=1;
                }
                if(p.getPower().isEmpty()&&danger==0){
                    danger=0;
                }
                log.info(" / 위험도 : " + danger + "파워 : "+ p);
            }
        }
        if(checkPoint==1)
        {
            presentDate=presentDate.plusHours(3);
        }
        presentDate=presentDate.plusHours(3);
        return new Message(StatusEnum.OK, "위험도 : 0 = happy / 1 = sad / 2 = angry " , danger);
    }

    @PostMapping("/information/iot")
    public Message getIotDanger(){
        log.info("시작차수벽 : "+preWallUpDown);
        String danger = "wallAlertDeactivation";
        int point = 0;
        log.info("차수벽: "+presentDate.toString());
        presentDate=presentDate.minusHours(3);
        log.info("차수벽: "+presentDate.toString());
        List<PastTypdto> pastTypdtos = pastTypService.getPastTyp(presentDate);
        if(pastTypdtos.isEmpty())
        {
            presentDate=presentDate.minusHours(3);
            point = 1;
        }
        pastTypdtos = new ArrayList<>();
        pastTypdtos.addAll(pastTypService.getPastTyp(presentDate));

        for(PastTypdto p:pastTypdtos)
        {
            if(p.getAfter_time()<=24)
            {
                try {
                    p.getPower().equals("");
                }catch (Exception e)
                {
                    continue;
                }
                if(p.getPower().equals("중")&&danger!="wallAlertActivation" || p.getPower().equals("강")||p.getPower().equals("매우 강")||p.getPower().equals("초강력"))
                {
                    danger="wallAlertActivation";
                }
                if(p.getPower().equals("약")&&danger!="wallAlertActivation"|| p.getPower().isEmpty()&&danger!="wallAlertActivation")
                {
                    danger="wallAlertDeactivation";
                }
                log.info("차수벽 행동 지시 : " + danger + "파워 : "+p);
            }
        }

        Message message = new Message(StatusEnum.OK, "Successful", danger);
        if(preWallUpDown!=danger) {
            preWallUpDown = danger;
            Mono<String> response = client
                    .method(HttpMethod.POST)
                    .uri(enterAddress)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(message))
                    .retrieve()
                    .bodyToMono(String.class);
            String responseBody = response.block();
            log.info(responseBody);
        }
        if(point==1)
        {
            presentDate=presentDate.plusHours(3);
        }
        presentDate=presentDate.plusHours(3);
        log.info("끝차수벽:"+preWallUpDown);
        return message;
    }
}
