package com.hfad.myplanner;

public class SearchViewItem {
    private String C_NAME ;
    private String CATEGORY;
    private String CSDATE;
    private String JSDATE;
    private String JEDATE;

    public void setC_NAME(String C_NAME) {
        this.C_NAME = C_NAME;
    }
    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }
    public void setCSDATE(String CSDATE){
        this.CSDATE = CSDATE;
    }
    public void setJSDATE(String JSDATE) {this.JSDATE = JSDATE; }
    public void setJEDATE(String JEDATE) {this.JEDATE = JEDATE; }


    public String getC_NAME() {
        return C_NAME ;
    }
    public String getCATEGORY() {
        return CATEGORY;
    }
    public String getCSDATE() {
        return CSDATE ;
    }
    public String getJSDATE() { return JSDATE; }
    public String getJEDATE() { return JEDATE; }
}