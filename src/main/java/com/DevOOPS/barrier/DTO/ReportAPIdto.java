package com.DevOOPS.barrier.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ReportAPIdto { //front.
    private int stnId;
    private String title;
    private Date tmFc; //날짜
    private int tmSeq;

    private String regionData;

    public ReportAPIdto(int stnId, String title, Date tmFc,
                        int tmSeq, String regionData ) {
        this.stnId = stnId;
        this.title = title;
        this.tmFc = tmFc;
        this.tmSeq = tmSeq;
        this.regionData = regionData;
    }



}
