package com.DevOOPS.barrier.Controller;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.TyphoonInfoDTO;
import com.DevOOPS.barrier.DTO.WallDTO;
import com.DevOOPS.barrier.Exception.TyphoonInfoNullException;
import com.DevOOPS.barrier.Exception.TyphoonSearchException;
import com.DevOOPS.barrier.Service.AdminService;
import com.DevOOPS.barrier.Status.Message;
import com.DevOOPS.barrier.Status.StatusEnum;
import jdk.jshell.Snippet;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api")

public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/load") //예 특보 test test
    public Message postReportAPI() throws TyphoonSearchException {
        List<ReportAPIdto> reportAPIdtoResultList = new ArrayList<>();

        reportAPIdtoResultList = adminService.load_save();
        Message message = new Message(StatusEnum.OK,"성공",reportAPIdtoResultList); //IoT 서버와 연결했을 때 Http 통신 코드를 받아와서 적을 것.
        return message;
    }
    @GetMapping("/IoT")
    public Message postIoTReportAPI() throws TyphoonSearchException {
        List<WallDTO> wallDTOList = new ArrayList<WallDTO>();
        wallDTOList = adminService.IoTReportAPI();
        Message message = new Message(StatusEnum.OK, "IoT 서버와 통신 완료", wallDTOList);
        return message;
    }

    @GetMapping("TyphoonInfo")
    public Message postTyphoonInfo(@RequestParam("TypPower")int TypPower) throws TyphoonSearchException, TyphoonInfoNullException {
        String TypPowerValue;
        TypPowerValue = adminService.PostTyphoonInfo(TypPower);
        Message message = new Message(StatusEnum.OK, "Successful post TyphoonInfo.", TypPowerValue);

        return message;

    }
        @ExceptionHandler({TyphoonSearchException.class})
        public Message handleException(Exception ex) {
            log.warn(ex.toString());
            return new Message(); //404에러
        }

        @ExceptionHandler({TyphoonInfoNullException.class})
        public Message TyphoonInfoNullHandleException(Exception ex) {

            return new Message(StatusEnum.INTERNAL_SERVER_ERROR, "비어있는 데이터에 접근하였습니다.");
    }
    }
