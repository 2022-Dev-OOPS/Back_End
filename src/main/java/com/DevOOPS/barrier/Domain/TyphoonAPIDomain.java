package com.DevOOPS.barrier.Domain;

public class TyphoonAPIDomain {
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

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTmFc() {
        return tmFc;
    }

    public void setTmFc(String tmFc) {
        this.tmFc = tmFc;
    }

    public int getTmSeq(){return tmSeq;}
    public void setTmSeq(int tmSeq){this.tmSeq = tmSeq;}

    public int getTypSeq(){return typSeq;}
    public void setTypSeq(int typSeq){this.typSeq = typSeq;}

    public String getTypEn(){return typEn;}
    public void setTypEn(String typEn){this.typEn = typEn;}

    public String getTypName(){return typName;}
    public void setTypName(String typName){this.typName = typName;}

    public String getTypTm(){return typTm;}
    public void setTypTm(String typTm){this.typTm = typTm;}

    public float getTypLat(){return typLat;}
    public void setTypLat(float typLat){this.typLat = typLat;}

    public float getTypLon(){return typLon;}
    public void setTypLon(float typLon){this.typLon = typLon;}

    public String getTypDir(){return typDir;}
    public void setTypDir(String typDir){this.typDir = typDir;}

    public int getTypWs(){return typWs;}
    public void setTypWs(int typWs){this.typWs = typWs;}

    public int getTyp25(){return typ25;}
    public void setTyp25(int typ25){this.typ25 = typ25;}
}
