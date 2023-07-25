package com.DevOOPS.barrier.DTO;

import java.time.LocalDateTime;

public class IoTDangerdto {
    private LocalDateTime typ_date;
    private  int after_time;
    private  double typ_lat;
    private  double typ_lon;
    private  Integer typrad;
    private  String power;

    public IoTDangerdto(LocalDateTime typ_date, int after_time, double typ_lat, double typ_lon,
                        Integer typrad, String power) {
        this.typ_date = typ_date;
        this.after_time = after_time;
        this.typ_lat = typ_lat;
        this.typ_lon = typ_lon;
        this.typrad = typrad;
        this.power = power;
    }
    public  int getAfter_time() {
        return after_time;
    }
    public  double getTyp_lat(){
        return typ_lat;
    }
    public  double getTyp_lon(){
        return typ_lon;
    }
    public  Integer getTyprad(){
        return typrad;
    }
    public  String getPower(){
        return power;
    }
}
