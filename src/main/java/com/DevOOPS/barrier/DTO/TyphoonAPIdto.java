package com.DevOOPS.barrier.DTO;

import lombok.Data;

@Data
public class TyphoonAPIdto {
    private int idx;
    private String tmFc;
    private int tmSeq;
    private int typSeq;
    private String typEn;
    private String typName;
    private String typTm;
    private float typLat;
    private float typLon;
    private String typDir;
    private int typWs;
    private int typ25;

    public TyphoonAPIdto(int idx, String tmFc, int tmSeq, int typSeq, String typEn, String typName, String typTm,
                         float typLat, float typLon, String typDir, int typWs, int typ25) {
        this.idx = idx;
        this.tmFc = tmFc;
        this.tmSeq = tmSeq;
        this.typSeq = typSeq;
        this.typEn = typEn;
        this.typName = typName;
        this.typTm = typTm;
        this.typLat = typLat;
        this.typLon = typLon;
        this.typDir = typDir;
        this.typWs = typWs;
        this.typ25 = typ25;
    }


}