package com.DevOOPS.barrier.Mapper;

import com.DevOOPS.barrier.DTO.ReportAPIdto;
import com.DevOOPS.barrier.DTO.dto;
import com.DevOOPS.barrier.Domain.admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
public interface AdminMapper {

    public void createAdmin(dto dt);
    public void deleteAdmin (String adminId);
    List<dto> getAdminAll = null;

    public void ReportAPICall (ReportAPIdto apIdto);
}