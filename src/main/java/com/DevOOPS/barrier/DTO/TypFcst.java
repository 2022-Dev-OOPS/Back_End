package com.DevOOPS.barrier.DTO;

public class TypFcst {
    public TypFcst(String get_dir, String get_ed15, double get_lat, double get_lon, int get_rad15, String get_tm, String get_tmFc, int get_ws) {
        this.get_dir = get_dir;
        this.get_ed15 = get_ed15;
        this.get_lat = get_lat;
        this.get_lon = get_lon;
        this.get_rad15 = get_rad15;
        this.get_tm = get_tm;
        this.get_tmFc = get_tmFc;
        this.get_ws = get_ws;
    }

    private String get_dir;
    private String get_ed15;
    private int get_er15;
    private double get_lat;
    private double get_lon;
    private int get_rad15;
    private String get_tm;
    private String get_tmFc;
    private int get_ws;

    public String getGet_dir() {
        return get_dir;
    }

    public void setGet_dir(String get_dir) {
        this.get_dir = get_dir;
    }

    public String getGet_ed15() {
        return get_ed15;
    }

    public void setGet_ed15(String get_ed15) {
        this.get_ed15 = get_ed15;
    }

    public int getGet_er15() {
        return get_er15;
    }

    public void setGet_er15(int get_er15) {
        this.get_er15 = get_er15;
    }

    public double getGet_lat() {
        return get_lat;
    }

    public void setGet_lat(float get_lat) {
        this.get_lat = get_lat;
    }

    public double getGet_lon() {
        return get_lon;
    }

    public void setGet_lon(float get_lon) {
        this.get_lon = get_lon;
    }


    public int getGet_rad15() {
        return get_rad15;
    }

    public void setGet_rad15(int get_rad15) {
        this.get_rad15 = get_rad15;
    }

    public String getGet_tm() {
        return get_tm;
    }

    public void setGet_tm(String get_tm) {
        this.get_tm = get_tm;
    }

    public String getGet_tmFc() {
        return get_tmFc;
    }

    public void setGet_tmFc(String get_tmFc) {
        this.get_tmFc = get_tmFc;
    }

    public int getGet_ws() {
        return get_ws;
    }

    public void setGet_ws(int get_ws) {
        this.get_ws = get_ws;
    }
}