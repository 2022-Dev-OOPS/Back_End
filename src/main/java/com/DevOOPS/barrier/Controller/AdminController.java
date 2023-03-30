package com.DevOOPS.barrier.Controller;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.Exception.TyphoonSearchException;
import com.DevOOPS.barrier.Service.AdminService;
import com.DevOOPS.barrier.Status.Message;
import com.DevOOPS.barrier.Status.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api")

public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/load")
    public Message postReportAPI() throws TyphoonSearchException {
        List<ReportAPIdto> reportAPIdtoResultList = new ArrayList<>();

        reportAPIdtoResultList = adminService.load_save();
        Message message = new Message(StatusEnum.OK,"성공",reportAPIdtoResultList); //IoT 서버와 연결했을 때 Http 통신 코드를 받아와서 적을 것.
        return message;
    }
        @ExceptionHandler({TyphoonSearchException.class})
        public Message handleException(Exception ex) {
            log.warn(ex.toString());
            return new Message(); //404에러
        }
    }