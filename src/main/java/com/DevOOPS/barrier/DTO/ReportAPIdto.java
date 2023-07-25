package com.DevOOPS.barrier.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ReportAPIdto { //front.
    private int stnId;
    private Date tmFc; //날짜
    private int tmSeq;
    private String regionData;
    private String title1;
    private String title2;

    public ReportAPIdto(int stnId, Date tmFc,
                        int tmSeq, String regionData, String title1, String title2 ) {
        this.stnId = stnId;
        this.tmFc = tmFc;
        this.tmSeq = tmSeq;
        this.regionData = regionData;
        this.title1 = title1;
        this.title2 = title2;
    }



}
