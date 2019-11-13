package com.hfad.myplanner;

public class ListViewComment {
    public int getCM_NUM() {
        return CM_NUM;
    }

    private int CM_NUM;

    public void setCM_NUM(int CM_NUM) {
        this.CM_NUM = CM_NUM;
    }

    private String CM_TEXT;

    public String getID() {
        return ID;
    }

    public String getDATE() {
        return DATE;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    private String ID;
    private String DATE;

    public void setCM_TEXT(String CM_TEXT){ this.CM_TEXT = CM_TEXT;}

    public String getCM_TEXT() {
        return CM_TEXT;
    }
}
