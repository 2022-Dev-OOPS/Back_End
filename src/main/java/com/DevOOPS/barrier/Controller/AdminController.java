package com.DevOOPS.barrier.Controller;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.TypFcst;
import com.DevOOPS.barrier.DTO.TyphoonInfoDTO;
import com.DevOOPS.barrier.DTO.WallDTO;
import com.DevOOPS.barrier.Exception.TyphoonInfoNullException;
import com.DevOOPS.barrier.Exception.TyphoonSearchException;
import com.DevOOPS.barrier.Service.TyphoonService;
import com.DevOOPS.barrier.Status.Message;
import com.DevOOPS.barrier.Status.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api")

public class AdminController {
    @Autowired
    TyphoonService typhoonService;
    @GetMapping("/test")
    public Message test() throws UnsupportedEncodingException {
        List<TypFcst> getTypFcstList = new ArrayList<>();
        String getTypList = typhoonService.getTyphoonInfoList();
        String[] List = getTypList.split(",");
        getTypFcstList = typhoonService.getTyphoonFcst(List[0], List[1]);

        Message message= new Message(StatusEnum.OK, "성공", getTypFcstList);
        return message;
    }
    @GetMapping("/load") //예 특보 test test
    public Message postReportAPI() throws TyphoonSearchException {
        List<ReportAPIdto> reportAPIdtoResultList = new ArrayList<>();
        reportAPIdtoResultList = typhoonService.load_save();

        Message message = new Message(StatusEnum.OK,"성공",reportAPIdtoResultList); //IoT 서버와 연결했을 때 Http 통신 코드를 받아와서 적을 것.
        return message;


    }
    @PostMapping("/IoT")
    public Message postIoTReportAPI() throws TyphoonSearchException {
        List<WallDTO> wallDTOList = new ArrayList<WallDTO>();
        WallDTO wallDTOResult = new WallDTO(false, false, 0);
        wallDTOResult = typhoonService.IoTReportAPI();

        Message message = new Message(StatusEnum.OK, "IoT 서버와 통신 완료", wallDTOResult);
        return message;
    }

    @GetMapping("TyphoonInfo")
    public Message postTyphoonInfo() throws TyphoonSearchException, TyphoonInfoNullException {
        TyphoonInfoDTO getReportTyphoonData;
        getReportTyphoonData = typhoonService.PostTyphoonInfo();

        Message message = new Message(StatusEnum.OK, "Successful post TyphoonInfo.", getReportTyphoonData);
        return message;
    }

    @GetMapping("khoaObsWaveHeight")
    public Message getObsWaveHeight() {
        typhoonService.getObsWaveHeight();

        Message message = new Message(StatusEnum.OK, "Success", "good");
        return message;
    }
    @ExceptionHandler({TyphoonSearchException.class})
    public Message handleException(Exception ex) {
        log.warn(ex.toString());
        return new Message(); //404에러
    }
    @ExceptionHandler({TyphoonInfoNullException.class})
    public Message TyphoonInfoNullHandleException(Exception ex){
        log.warn(ex.toString());
        return new Message(StatusEnum.INTERNAL_SERVER_ERROR,"비어있는 데이터에 접근하였습니다.");
        }
}
