package com.DevOOPS.barrier.DTO;

import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class PastTypdto {
    private LocalDateTime typ_date;      //발표 날짜
    private int after_time; //예측 시간
    private String typ_name; //태풍명
    private String power;   //태풍 강도
    private String typ_size;    //태풍 크기

    public PastTypdto(LocalDateTime typ_date, int after_time, String typName, String power, String typ_size){
        this.typ_date = typ_date;
        this.after_time = after_time;
        this.typ_name = typName;
        this.power = power;
        this.typ_size = typ_size;
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
    public String getPower(){
        return power;
    }
    public String getTyp_size(){
        return typ_size;
    }
}
