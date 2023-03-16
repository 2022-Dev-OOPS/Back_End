package com.DevOOPS.barrier.DAO;

import lombok.Data;

import java.util.Date;

@Data
public class ReportAPIdao {
    private int stnId;
    private String title;
    private Date tmFc;
    private int tmSeq;

    public ReportAPIdao( int stnId, String title, Date tmFc,
                        int tmSeq ) {
        this.stnId = stnId;
        this.title = title;
        this.tmFc = tmFc;
        this.tmSeq = tmSeq;
    }



}
