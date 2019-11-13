package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WriteBoardRequest extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/CreateBoard.php";
    private Map<String, String> parameters;

    public WriteBoardRequest(String B_TITLE, String B_TEXT,int U_NUM, String C_NUM,String date,Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("B_TITLE",B_TITLE);
        parameters.put("B_TEXT",B_TEXT);
        parameters.put("U_NUM",U_NUM + "");
        parameters.put("C_NUM",C_NUM + "");
        parameters.put("B_DATE",date);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
