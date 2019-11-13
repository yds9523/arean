package com.hfad.myplanner;

public class ListViewBoard {
    private int B_NUM;
    private String B_TITLE;
    private String B_TEXT;

    public String getID() {
        return ID;
    }

    public String getB_DATE() {
        return B_DATE;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setB_DATE(String b_DATE) {
        B_DATE = b_DATE;
    }

    private String ID;
    private String B_DATE;

    public void setB_NUM(int B_NUM){ this.B_NUM = B_NUM; }
    public void setB_TITLE(String B_TITLE){ this.B_TITLE = B_TITLE; }
    public void setB_TEXT(String B_TEXT){ this.B_TEXT = B_TEXT; }

    public int getB_NUM() {
        return B_NUM;
    }
    public String getB_TITLE() {
        return B_TITLE;
    }
    public String getB_TEXT() {
        return B_TEXT ;
    }
}
