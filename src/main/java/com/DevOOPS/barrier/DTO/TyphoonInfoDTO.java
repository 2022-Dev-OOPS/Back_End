package com.DevOOPS.barrier.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
public class TyphoonInfoDTO {
    private String img;
    private String tmFc;
    private String typSeq;
    private String tmSeq;
    private String typTm;
    private float typLat;
    private float typLon;
    private String typLoc;
    private String typDir;
    private float typSp;
    private float typPs;
    private float typWs;
    private float typ15;
    private float typ25;
    private String typName;
    private String typEn;
    private String rem;
    private String other;

    public TyphoonInfoDTO(String img, String tmFc, String typSeq, String tmSeq, String typTm, float typLat,
                          float typLon, String typLoc, String typDir, float typSp, float typPs, float typWs,
                          float typ15, float typ25, String typName, String typEn, String rem, String other) {
        this.img = img;
        this.tmFc = tmFc;
        this.typSeq = typSeq;
        this.tmSeq = tmSeq;
        this.typTm = typTm;
        this.typLat = typLat;
        this.typLon = typLon;
        this.typLoc = typLoc;
        this.typDir = typDir;
        this.typSp = typSp;
        this.typPs = typPs;
        this.typWs = typWs;
        this.typ15 = typ15;
        this.typ25 = typ25;
        this.typName = typName;
        this.typEn = typEn;
        this.rem = rem;
        this.other = other;
    }
}
