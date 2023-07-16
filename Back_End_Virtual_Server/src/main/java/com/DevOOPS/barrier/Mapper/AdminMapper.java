package com.DevOOPS.barrier.Mapper;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.dto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface AdminMapper {

    public void createAdmin(dto dt);
    public void deleteAdmin (String adminId);
    List<dto> getAdminAll = null;

    public void ReportAPICall (ReportAPIdto apIdto);

    public String RegionData (int stnId);
}