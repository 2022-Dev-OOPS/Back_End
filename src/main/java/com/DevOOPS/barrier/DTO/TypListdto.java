package com.DevOOPS.barrier.DTO;

import java.time.LocalDateTime;

public class TypListdto {
    private int idx;
    private String typName;
    private LocalDateTime typStart;
    private LocalDateTime typEnd;

    public TypListdto(int idx, String typName, LocalDateTime typStart, LocalDateTime typEnd){
        this.idx=idx;
        this.typName=typName;
        this.typStart = typStart;
        this.typEnd = typEnd;
    }

    public int getidx(){
        return idx;
    }
    public String getTypName(){
        return typName;
    }
    public LocalDateTime gettypStart() {
        return typStart;
    }
    public LocalDateTime gettypEnd(){
        return typEnd;
    }
}
