package com.DevOOPS.barrier.DTO;

import lombok.Data;

@Data
public class ReportAPIdto {
    private int idx;
    private int stnId;
    private String title;
    private String tmFc;
    private int tmSeq;

    public ReportAPIdto(int idx, int stnId, String title, String tmFc,
                        int tmSeq ) {
        this.idx = idx;
        this.stnId = stnId;
        this.title = title;
        this.tmFc = tmFc;
        this.tmSeq = tmSeq;
    }



}
