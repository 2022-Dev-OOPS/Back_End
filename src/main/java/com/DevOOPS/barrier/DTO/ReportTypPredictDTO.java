package com.DevOOPS.barrier.DTO;

public class ReportTypPredictDTO {
    public ReportTypPredictDTO(String tm, double lat, double lon, double radPr, int ws, int ps, String fcLocKo, int rad15, String dir, int sp, String tmFc, int seq) {
        this.tm = tm;
        this.lat = lat;
        this.lon = lon;
        this.radPr = radPr;
        this.ws = ws;
        this.ps = ps;
        this.fcLocKo = fcLocKo;
        this.rad15 = rad15;
        this.dir = dir;
        this.sp = sp;
        this.tmFc = tmFc;
        this.seq = seq;
    }

    private String tm;
    private double lat;
    private double lon;
    private double radPr;
    private int ws;
    private int ps;
    private String fcLocKo;
    private int rad15;
    private String dir;
    private int sp;
    private String tmFc;
    private int seq;

}