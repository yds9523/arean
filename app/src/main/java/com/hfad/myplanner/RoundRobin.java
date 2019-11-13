package com.hfad.myplanner;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RoundRobin extends StringRequest {

    final static private String URL = "http://imhost.iptime.org/RouondRobin.php";
    private Map<String, String> parameters;

    public RoundRobin(String C_NUM, int MEMBER,String data, String title, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("C_NUM",C_NUM + "");
        parameters.put("MEMBER",MEMBER + "");
        parameters.put("DATA",data);
        parameters.put("TITLE",title);
    }

    @Override
    public Map<String, String> getParams() { return parameters; }
}
