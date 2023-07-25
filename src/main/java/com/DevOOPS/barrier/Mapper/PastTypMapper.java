package com.DevOOPS.barrier.Mapper;

import com.DevOOPS.barrier.DTO.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Mapper
public interface PastTypMapper {

    public TypListdto getTypList(int idx);

    public List<PastTypdto> getPastTyp(LocalDateTime typ_date);

    public List<TypDatadto> getTypData(LocalDateTime typ_date);
    public List<TypDangerdto> getTypDanger(LocalDateTime typ_date);

    public List<IoTDangerdto> getIotDanger(LocalDateTime typ_date);
}
