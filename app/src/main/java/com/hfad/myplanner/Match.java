package com.hfad.myplanner;

public class Match {
    public String getNmae() {
        return nmae;
    }

    public String getDate() {
        return date;
    }

    public String getT1() {
        return t1;
    }

    public String getT2() {
        return t2;
    }

    private String nmae;

    public void setNmae(String nmae) {
        this.nmae = nmae;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    private String date;
    private String t1;
    private String t2;
    private String r1;
    private int to;

    public void setTo(int to) {
        this.to = to;
    }

    public int getTo() {
        return to;
    }

    public String getR1() {
        return r1;
    }

    public String getR2() {
        return r2;
    }

    public String getWin() {
        return win;
    }

    private String r2;

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public void setWin(String win) {
        this.win = win;
    }

    private String win;

    public int getG_num() {
        return g_num;
    }

    public void setG_num(int g_num) {
        this.g_num = g_num;
    }

    private int g_num;

}
