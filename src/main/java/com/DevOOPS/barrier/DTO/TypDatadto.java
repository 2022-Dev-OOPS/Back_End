package com.DevOOPS.barrier.DTO;

import java.time.LocalDateTime;

public class TypDatadto {
    private int id;
    private LocalDateTime typ_date;
    private int after_time;
    private String typ_name;
    private double typ_lat;
    private double typ_lon;
    private int typ_ps;
    private int mper_sec;
    private int kmper_hour;
    private int typrad;
    private String power;
    private String typ_size;
    private String typ_dir;
    private int typ_sp;
    private Integer rad_pr; //int는 null 사용 불가임

    public TypDatadto(int id, LocalDateTime typ_date, int after_time, String typ_name, double typ_lat, double typ_lon, int typ_ps,
                      int mper_sec, int kmper_hour, int typrad, String power, String typ_size, String typ_dir, int typ_sp, Integer rad_pr){
        this.id = id;
        this.typ_date = typ_date;
        this.after_time = after_time;
        this.typ_name = typ_name;
        this.typ_lat = typ_lat;
        this.typ_lon = typ_lon;
        this.typ_ps = typ_ps;
        this.mper_sec = mper_sec;
        this.kmper_hour = kmper_hour;
        this.typrad = typrad;
        this.power = power;
        this.typ_size = typ_size;
        this.typ_dir = typ_dir;
        this.typ_sp = typ_sp;
        this.rad_pr = rad_pr;
    }
    public int getId(){
        return id;
    }
    public LocalDateTime getTyp_date(){
        return typ_date;
    }
    public int getAfter_time(){
        return after_time;
    }
    public String getTyp_name(){
        return typ_name;
    }
    public double getTyp_lat(){
        return typ_lat;
    }
    public double getTyp_lon(){
        return typ_lon;
    }
    public int getTyp_ps(){
        return typ_ps;
    }
    public int getMper_sec(){
        return mper_sec;
    }
    public int getKmper_hour(){
        return kmper_hour;
    }
    public int getTyprad(){
        return typrad;
    }
    public String getPower(){
        return power;
    }
    public String getTyp_size(){
        return typ_size;
    }
    public String getTyp_dir(){
        return typ_dir;
    }
    public int getTyp_sp(){
        return typ_sp;
    }
    public Integer getRad_pr(){
        return rad_pr;
    }
}
