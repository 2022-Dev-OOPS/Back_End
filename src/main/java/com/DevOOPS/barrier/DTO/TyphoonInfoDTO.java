package com.DevOOPS.barrier.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
public class TyphoonInfoDTO {
    private String tmFc;
    private String typSeq;
    private String tmSeq;
    private String typTm;
    private double typLat;
    private double typLon;
    private String typLoc;
    private String typDir;
    private double typSp;
    private double typPs;
    private double typWs;
    private double typ15;
    private double typ25;
    private String typName;
    private String typEn;

    public TyphoonInfoDTO(String tmFc, String typSeq, String tmSeq, String typTm, double typLat,
                          double typLon, String typLoc, String typDir, double typSp, double typPs, double typWs,
                          double typ15, double typ25, String typName, String typEn) {
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
    }
}
