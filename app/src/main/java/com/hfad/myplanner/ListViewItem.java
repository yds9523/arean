package com.hfad.myplanner;

public class ListViewItem {
    private String C_NAME ;
    private String CATEGORY;
    private String CSDATE;
    private String JSDATE;
    private String JEDATE;
    private String C_TEXT;
    private String C_NUM;
    private int MEMBER;
    private String S_MEMBER;
    private String FORM;
    private int U_NUM;

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    private String LOCATION;

    public int getU_NUM() {
        return U_NUM;
    }

    public void setU_NUM(int u_NUM) {
        U_NUM = u_NUM;
    }


    public void setC_NAME(String C_NAME) {
        this.C_NAME = C_NAME;
    }
    public void setCATEGORY(String CATEGORY) { this.CATEGORY = CATEGORY; }
    public void setCSDATE(String CSDATE){
        this.CSDATE = CSDATE;
    }
    public void setJSDATE(String JSDATE){
        this.JSDATE = JSDATE;
    }
    public void setJEDATE(String JEDATE){
        this.JEDATE = JEDATE;
    }
    public void setC_TEXT(String C_TEXT){
        this.C_TEXT = C_TEXT;
    }
    public void setC_NUM(String C_NUM){
        this.C_NUM = C_NUM;
    }
    public void setMEMBER(int MEMBER){
        this.MEMBER = MEMBER;
    }
    public void setS_MEMBER(String S_MEMBER){
        this.S_MEMBER = S_MEMBER;
    }
    public void setFORM(String FORM){
        this.FORM = FORM;
    }

    public String getC_NAME() {
        return C_NAME ;
    }
    public String getCATEGORY() {
        return CATEGORY;
    }
    public String getCSDATE() {
        return CSDATE ;
    }
    public String getJSDATE() {
        return JSDATE ;
    }
    public String getJEDATE() {
        return JEDATE ;
    }
    public String getC_TEXT() {
        return C_TEXT ;
    }
    public String getC_NUM() {
        return C_NUM ;
    }
    public int getMEMBER() {
        return MEMBER ;
    }
    public String getS_MEMBER() {
        return S_MEMBER ;
    }
    public String getFORM() {
        return FORM ;
    }
}