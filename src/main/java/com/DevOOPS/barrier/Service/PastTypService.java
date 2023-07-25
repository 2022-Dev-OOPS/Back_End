package com.DevOOPS.barrier.Service;


import com.DevOOPS.barrier.DTO.*;
import com.DevOOPS.barrier.Mapper.PastTypMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PastTypService {

    private  PastTypMapper pastTypMapper;
    @Autowired
    public PastTypService(PastTypMapper pastTypMapper) {
        this.pastTypMapper = pastTypMapper;
    }

    public  TypListdto getTypList(int idx) {
        return pastTypMapper.getTypList(idx);
    }

    public  List<PastTypdto> getPastTyp(LocalDateTime typ_date) {return pastTypMapper.getPastTyp(typ_date);}

}
